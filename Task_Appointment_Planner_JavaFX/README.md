# Task & Appointment Planner (JavaFX Final Project)

This repo contains:
- Core model classes based on the approved UML (no UI code in model classes)
- A JavaFX GUI (`MainApp`) with event-driven interactions (Add/Edit/Delete/Mark Complete/Load/Save)
- A console test runner (`ConsoleTestRunner`) for Update 3 style testing output
- A Word doc template with UML + screenshot placeholders in `docs/`

## Folder layout
- `src/`  Java source (including JavaFX GUI)
- `test/` Console test runner
- `docs/` Word documentation (UML + screenshot placeholders)
- `lib/`  Notes (optional JUnit jars)

## Running the JavaFX app
You must have **JavaFX SDK** installed and configured in your IDE.

### VM Options (typical)
```
--module-path "PATH_TO_FX/lib" --add-modules javafx.controls
```

### Main class
`MainApp`

## Data storage
- Uses text file storage via `FileStorageManager` with delimiter `|`
- Default file location:
  - Windows: C:\Users\<you>\planner_data.txt
  - Mac/Linux: /Users/<you>/planner_data.txt
- Use **File -> Load** and **File -> Save As** to choose a different file.

## Console testing (Update 3 style)
Run `ConsoleTestRunner` (in `test/`) and take a screenshot of the console output for your documentation.

## Compile from command line (optional)
If you compile outside the IDE, JavaFX module-path settings vary by OS.
Your instructor will typically open the project in an IDE and set JavaFX SDK.
