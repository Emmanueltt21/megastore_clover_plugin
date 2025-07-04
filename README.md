# 🛍️ MegaStore - Custom Order Integration for Clover POS

**MegaStore** is a powerful Android application that integrates directly with Clover POS devices to enable merchants to create, view, and manage custom orders with support for inventory, payment processing, and Register app integration.

---

## 📱 Features

- ✅ **Clover Register Integration**: Appears as a button in the Clover Register UI like "Dave's Order".
- 🛒 **Custom Order Creation**: Create orders with customer name, special instructions, and attach inventory items.
- 💳 **Clover Payment Integration**: Seamlessly launch Clover's payment UI to complete transactions.
- 📦 **Inventory Management**: Fetch items from Clover’s inventory and associate them with orders.
- 📊 **Order History**: View count and details of custom orders created through MegaStore.
- 🔐 **Secure Clover SDK Communication**: Uses OrderConnector and InventoryConnector with proper lifecycle handling.
- 🔔 **Push Notifications Ready**: Supports notification permissions and background tasks.
- 🛠️ **Clean Architecture Principles**: Modular activity setup with separation of concerns.

---

## 📸 Screenshots

| Register Integration Button | Custom Order Screen |
|----------------------------|---------------------|
| ![Register](./screenshots/register_button.png) | ![Order](./screenshots/custom_order.png) |

---

## 🧠 Architecture

- Language: **Java**
- Build System: **Gradle (KTS DSL)**
- SDK: **Clover Android SDK**
- Target SDK: **33** (Play Store & Clover-compliant)
- Min SDK: **21** (Clover supports API 24+)
- State: **Stateless activities**, async tasks, and service connectors
- Code Quality: Lint, modular structure, and build types for `debug` and `release`

---

## ⚙️ Setup Instructions

### Prerequisites

- Android Studio Hedgehog or later
- Java 11+
- Clover Dev Account: https://sandbox.dev.clover.com/
- Clover device or emulator (with Register app installed)

### Clone and Build

```bash
git clone https://github.com/your-org/megastore-clover.git
cd megastore-clover
./gradlew clean build
```

### Clover Register Integration

To enable the MegaStore button inside Clover Register:

1. Ensure your `AndroidManifest.xml` includes:
    ```xml
    <intent-filter>
        <action android:name="clover.intent.action.REGISTER_INTEGRATION" />
        <category android:name="android.intent.category.DEFAULT" />
    </intent-filter>
    ```

2. Label and icon define the button text and image:
    ```xml
    android:label="MegaStore"
    android:icon="@drawable/ic_mega_store"
    ```

3. Deploy via USB or Play Store (for production, sign properly).

---

## 🔐 Permissions Used

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="com.clover.permission.CLOVER_APPMARKET" />
<uses-permission android:name="clover.permission.ORDERS_R" />
<uses-permission android:name="clover.permission.ORDERS_W" />
<uses-permission android:name="clover.permission.INVENTORY_R" />
```

---

## 📁 Project Structure

```
📦 com.loyalty.api18
├── MainActivity.java              // Entry launcher
├── CustomOrderActivity.java       // Handles custom order creation and payment
├── RegisterIntegrationActivity.java // Register button launcher handler
├── /res
│   ├── layout/
│   └── drawable/
├── AndroidManifest.xml
├── build.gradle.kts              // Kotlin DSL build configuration
```

---

## 🚀 Roadmap

- [ ] Add QR code scanning for item lookup
- [ ] Multi-item selection support
- [ ] Persist custom orders in external DB (e.g., Firebase or Supabase)
- [ ] Add Unit & UI Tests
- [ ] Migrate to Kotlin (Optional)

---

## 🧪 Testing

- Tested on: **Clover Mini**, **Clover Station**
- Ensure Clover POS has inventory items loaded for full test coverage.

---

## 🧑‍💻 Developed By

**Taah Emmanuel**  
Senior Android Developer  
🔗 

---

## 📄 License

This project is licensed under the [MIT License](LICENSE).

---

## 🤝 Contributions

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

---

## 📬 Contact

For issues, suggestions, or business inquiries:  
📧 ttemmanuel2020@gmail.com
