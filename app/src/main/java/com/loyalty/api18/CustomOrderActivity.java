package com.loyalty.api18;

import android.app.Activity;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.clover.sdk.util.CloverAccount;
import com.clover.sdk.v1.BindingException;
import com.clover.sdk.v1.ClientException;
import com.clover.sdk.v1.Intents;
import com.clover.sdk.v1.ResultStatus;
import com.clover.sdk.v1.ServiceException;
import com.clover.sdk.v3.inventory.PriceType;
import com.clover.sdk.v3.order.Order;
import com.clover.sdk.v3.order.OrderConnector;
import com.clover.sdk.v3.inventory.InventoryConnector;
import com.clover.sdk.v3.inventory.Item;
import android.accounts.Account;
import android.content.Intent;
import android.os.AsyncTask;
import java.util.List;

public class CustomOrderActivity extends Activity {
    private static final String TAG = "CustomOrderActivity";

    private Account cloverAccount;
    private OrderConnector orderConnector;
    private InventoryConnector inventoryConnector;

    private EditText etCustomerName;
    private EditText etSpecialInstructions;
    private Button btnCreateOrder;
    private Button btnPayOrder;
    private Button btnViewOrders;

    private Order currentOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_order);

        initializeViews();
        setupCloverConnection();
        setupClickListeners();
    }

    private void initializeViews() {
        etCustomerName = findViewById(R.id.et_customer_name);
        etSpecialInstructions = findViewById(R.id.et_special_instructions);
        btnCreateOrder = findViewById(R.id.btn_create_order);
        btnPayOrder = findViewById(R.id.btn_pay_order);
        btnViewOrders = findViewById(R.id.btn_view_orders);

        // Initially disable pay button
        btnPayOrder.setEnabled(false);
    }

    private void setupCloverConnection() {
        cloverAccount = CloverAccount.getAccount(this);
        if (cloverAccount != null) {
            orderConnector = new OrderConnector(this, cloverAccount, null);
            inventoryConnector = new InventoryConnector(this, cloverAccount, null);

            orderConnector.connect();
            inventoryConnector.connect();
        }
    }

    private void setupClickListeners() {
        btnCreateOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createCustomOrder();
            }
        });

        btnPayOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentOrder != null) {
                    startRegisterIntent(true);
                } else {
                    Toast.makeText(CustomOrderActivity.this,
                            "Please create an order first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnViewOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewCustomOrders();
            }
        });
    }

    private void createCustomOrder() {
        String customerName = etCustomerName.getText().toString().trim();
        String specialInstructions = etSpecialInstructions.getText().toString().trim();

        if (customerName.isEmpty()) {
            Toast.makeText(this, "Please enter customer name", Toast.LENGTH_SHORT).show();
            return;
        }

        // Disable button during creation
        btnCreateOrder.setEnabled(false);

        // Create order in background
        new CreateOrderAsyncTask(customerName, specialInstructions).execute();
    }

    private void viewCustomOrders() {
        new ViewOrdersAsyncTask().execute();
    }

    // Start intent to launch Clover's register pay activity
    private void startRegisterIntent(boolean autoLogout) {
        Intent intent = new Intent(Intents.ACTION_CLOVER_PAY);
        intent.putExtra(Intents.EXTRA_CLOVER_ORDER_ID, currentOrder.getId());
        intent.putExtra(Intents.EXTRA_OBEY_AUTO_LOGOUT, autoLogout);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (orderConnector != null) {
            orderConnector.disconnect();
        }
        if (inventoryConnector != null) {
            inventoryConnector.disconnect();
        }
    }

    // Creates a new order with the first inventory item
    private class CreateOrderAsyncTask extends AsyncTask<Void, Void, Order> {
        private final String customerName;
        private final String specialInstructions;

        public CreateOrderAsyncTask(String customerName, String specialInstructions) {
            this.customerName = customerName;
            this.specialInstructions = specialInstructions;
        }

        @Override
        protected Order doInBackground(Void... params) {
            try {
                // Create new order
                Order order = new Order();
                order.setTitle("MegaStore's Custom Order - " + customerName);
                order.setNote(specialInstructions);
                order.setState("open");

                Order mOrder = orderConnector.createOrder(order);

                // Grab the items from the merchant's inventory
                List<Item> merchantItems = inventoryConnector.getItems();

                // If there are no items in the merchant's inventory, return order without items
                if (merchantItems.isEmpty()) {
                    runOnUiThread(() -> {
                        Toast.makeText(getApplicationContext(),
                                "No items in inventory. Order created without items.",
                                Toast.LENGTH_SHORT).show();
                    });
                    return mOrder;
                }

                // Taking the first item from the inventory
                Item mItem = merchantItems.get(0);

                // Add this item to the order based on its PriceType
                if (mItem.getPriceType() == PriceType.FIXED) {
                    orderConnector.addFixedPriceLineItem(mOrder.getId(), mItem.getId(), null, null);
                } else if (mItem.getPriceType() == PriceType.PER_UNIT) {
                    orderConnector.addPerUnitLineItem(mOrder.getId(), mItem.getId(), 1, null, null);
                } else { // The item must be of a VARIABLE PriceType
                    orderConnector.addVariablePriceLineItem(mOrder.getId(), mItem.getId(), 500, null, null);
                }

                return mOrder;
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException creating order", e);
            } catch (ClientException e) {
                Log.e(TAG, "ClientException creating order", e);
            } catch (ServiceException e) {
                Log.e(TAG, "ServiceException creating order", e);
            } catch (BindingException e) {
                Log.e(TAG, "BindingException creating order", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Order order) {
            // Re-enable button
            btnCreateOrder.setEnabled(true);

            if (!isFinishing()) {
                if (order != null) {
                    currentOrder = order;
                    btnPayOrder.setEnabled(true);

                    Toast.makeText(CustomOrderActivity.this,
                            "Order created successfully: " + order.getId(),
                            Toast.LENGTH_SHORT).show();

                    // Clear the form
                    etCustomerName.setText("");
                    etSpecialInstructions.setText("");
                } else {
                    Toast.makeText(CustomOrderActivity.this,
                            "Failed to create order",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    // View all orders
    private class ViewOrdersAsyncTask extends AsyncTask<Void, Void, List<Order>> {
        @Override
        protected List<Order> doInBackground(Void... params) {
            try {
                return orderConnector.getOrders(null);
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException getting orders", e);
            } catch (ClientException e) {
                Log.e(TAG, "ClientException getting orders", e);
            } catch (ServiceException e) {
                Log.e(TAG, "ServiceException getting orders", e);
            } catch (BindingException e) {
                Log.e(TAG, "BindingException getting orders", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Order> orders) {
            if (!isFinishing()) {
                if (orders != null) {
                    int customOrderCount = 0;
                    int totalOrders = orders.size();

                    for (Order order : orders) {
                        if (order.getTitle() != null &&
                                order.getTitle().startsWith("MegaStore's Custom Order")) {
                            customOrderCount++;
                        }
                    }

                    Toast.makeText(CustomOrderActivity.this,
                            "Found " + customOrderCount + " custom orders out of " + totalOrders + " total orders",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(CustomOrderActivity.this,
                            "Failed to retrieve orders",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}