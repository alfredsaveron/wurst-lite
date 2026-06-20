# Wurst Lite

<img src="images/wurstlite-banner.png" width="100%">

> [!IMPORTANT]
> Wurst Lite is an experimental, open-source project created solely for educational and research purposes, without any expectation of profit or financial income.

A clean, lightweight fork of the Wurst Client for Minecraft 1.21.1. Wurst Lite is optimized to provide essential cheat features with a streamlined interface, reduced bloat, and improved performance.

## Key Features

### Modern & Streamlined ClickGUI
Wurst Lite features a clean, organized, and responsive window-based ClickGUI. Overlapping issues have been eliminated, and window heights are tailored to display category hacks perfectly with scroll support.

<img src="images/wurstlite-gui.png" width="100%">

### Powerful Block Search
Find specific blocks easily with the built-in search visualization tool, highlighting targets cleanly.

<img src="images/wurstlite-searchhack.png" width="100%">

### AltLook & Camera Perspectives
Adjust your look and perspective smoothly while using movement and block-placing hacks like ScaffoldWalk, maintaining a completely legitimate look client-side and server-side.

<img src="images/altlook.gif" width="100%">

## Installation

Wurst Lite can be installed like any standard Fabric mod:

1. Run the Fabric installer for Minecraft **1.21.1**.
2. Download and place the **Wurst Lite** jar and **Fabric API** in your `.minecraft/mods` folder.
3. Launch Minecraft using the Fabric profile.

*Note: A licensed copy of Minecraft Java Edition is required.*

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

<img src="images/wurst-wurstlite.png" width="100%">

## Sources & Resources

For documentation, community assistance, and upstream codebases, refer to these references:

* **Official Website:** [wurstclient.net](https://www.wurstclient.net/)
* **Official Wiki & Docs:** [wurst.wiki](https://wurst.wiki/)
* **Upstream Wurst7 Repository:** [github.com/Wurst-Imperium/Wurst7](https://github.com/Wurst-Imperium/Wurst7)
* **Official Wurst Forum:** [wurstforum.net](https://wurstforum.net/)
* **Fabric Modding Toolchain:** [fabricmc.net](https://fabricmc.net/)

## License

This project is licensed under the GNU General Public License v3.
