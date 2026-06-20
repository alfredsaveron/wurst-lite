# Wurst Lite

![Wurst Lite Banner](images/wurstlite-banner.png)

A clean, lightweight fork of the Wurst Client for Minecraft. Wurst Lite aims to provide essential cheat features with a streamlined interface and performance optimizations.

---

## Installation

Wurst Lite can be installed like any standard Fabric mod:

1. Run the Fabric installer for Minecraft **1.21.1**.
2. Download and place the **Wurst Lite** jar and **Fabric API** in your `.minecraft/mods` folder.
3. Launch Minecraft using the Fabric profile.

*Note: A licensed copy of Minecraft Java Edition is required.*

---

## Development Setup

Ensure you have **Java Development Kit 21** installed.

### Workspace Setup

1. Clone the repository:
   ```pwsh
   git clone https://github.com/alfredsaveron/wurst-lite.git
   cd wurst-lite
   ```

2. Generate the resources for your preferred IDE:

   * **VSCode / Cursor:**
     ```pwsh
     ./gradlew genSources vscode
     ```
   * **IntelliJ IDEA:**
     ```pwsh
     ./gradlew genSources idea --no-configuration-cache
     ```
   * **Eclipse:**
     ```pwsh
     ./gradlew genSources eclipse
     ```

---

## Features & Configuration

More details on Wurst Lite's custom features and optimization settings will be added here over time.

---

## License

This project is licensed under the GNU General Public License v3.
