# PROJECT FILE: Wurst Lite (Vape-Style UI Redesign)

## 1. Project Overview
This project is a **custom client/UI overhaul** named **Wurst Lite**. It utilizes the open-source, Fabric-based infrastructure of the modern **Wurst Client (Wurst7)** (licensed under GPLv3) but completely strips away the legacy user interface (GUI) in favor of a modern, minimalist, and premium design language. The development phase will be securely hosted in a private GitHub repository.

## 2. Design Philosophy & UI/UX Goals
The old-school 2012 multi-window layout (ClickGUI) of the original Wurst Client will be completely abandoned. Instead, the design will draw heavy inspiration from **Vape Client** and modern web dashboard aesthetics:
* **Central Dashboard:** A single, sleek, centered control panel on the screen. The background will feature a semi-transparent, premium dark theme enhanced by an in-game background blur (shader effect).
* **Vertical Left Sidebar:** All cheat categories (Combat, Movement, Render, Player, etc.) will be consolidated into a vertical sidebar on the left side using minimalist icons. Switching between tabs must feature smooth alpha transitions (`fade-in`/`slide`).
* **Right Content Area (Bento Grid / Module Cards):** Modules belonging to the selected category will be listed on the right using an organized grid pattern with soft padding, clean margins, and rounded corners.
* **Modern Toggle Switches:** Activating/deactivating modules will abandon plain text clicks in favor of smooth, animated toggle switches using vibrant accent colors (Vape cyan/blue or soft pastel greens).
* **Color Palette:** A highly refined Dark Mode utilizing deep charcoal grays, rich ambers/blacks, and minimalist accent glows to keep it elegant and readable.

## 3. Technical Stack & Constraints
* **Minecraft Version:** 1.21.1+
* **Modding API:** Fabric API / Mixins
* **Language:** Java (Object-Oriented, clean, readable, and highly optimized code structure)
* **License Compliance:** The project will remain strictly **Private** during development. Upon public release/distribution, the codebase will be fully disclosed under the **GNU GPL v3.0** license, ensuring complete open-source transparency.

## 4. Expectations and AI Instructions
When generating code or architecture for this project, adhere strictly to the following parameters:
1. **Modern Minecraft Render Pipeline:** Base all GUI drawings on the modern Minecraft `DrawContext` and leverage Fabric Mixins where rendering pipelines need to be intercepted.
2. **Refactoring Legacy GUI:** Overwrite or intercept the old window-stacking behavior found in `wurst.client.gui` (e.g., `Window.java`, `ClickGui.java`) and implement a unified, object-oriented component system for the central dashboard.
3. **Performance Optimization:** Prevent FPS drops during rendering. Ensure geometry calculations (especially custom rounded rectangles and smooth shader blurs) are optimized and cached properly.

## 5. References & Official Links
* **Official Repository:** [Wurst-Imperium / Wurst7 (GitHub)](https://github.com/Wurst-Imperium/Wurst7)
* **Official Website:** [WurstClient.net](https://www.wurstclient.net/)
* **License Details:** [GNU GPL v3.0 License (GitHub)](https://github.com/Wurst-Imperium/Wurst7/blob/master/LICENSE)

---
*Note: This specification document serves as the absolute source of truth for the Wurst Lite project. All generated Java, Mixin configurations, or GLSL shader code must strictly align with this modern UI vision.*