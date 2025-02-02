import java.io.*; // Importing necessary I/O classes for file handling
import java.nio.file.Files; // Importing Files class for file operations
import java.nio.file.Path; // Importing Path class for file path operations
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList; // Importing ArrayList for dynamic array operations
import java.util.Scanner; // Importing Scanner for user input

public class NotesApp {
    private static String collectionName; // Variable to store the name of the notes collection
    private static final String NOTES_PATH = System.getProperty("user.dir") + "/notes/"; // Path to the notes directory
    private static final String NOTES_FILE = "notes.txt";
    private static ArrayList<Note> notes = new ArrayList<>(); // List to store notes
    private static final Scanner scanner = new Scanner(System.in); // Scanner for user input

    public static void main(String[] args) {
        if (args.length != 1 || args[0].equals("-h") || args[0].equals("--help")) { // Check if help is requested or incorrect arguments
            printUsage(); // Print usage information
            return;
        }

        collectionName = sanitizeFileName(args[0]); // Set the collection name from the arguments
        System.out.println("Welcome to the notes tool!\nCollection: " + collectionName); // Welcome message

        File notesFile = new File(NOTES_PATH + collectionName + "_" + NOTES_FILE);

        if (!setupNotesFile(notesFile)) { // Ensure the notes file is set up
            System.out.println("ERROR: Unable to initialize notes file."); // Error message if setup fails
            return;
        }

        notes = readNotesFromFile(notesFile); // Read existing notes from the file

        while (true) { // Main loop for the application
            showMenu(); // Display the menu
            String choice = scanner.nextLine().trim(); // Get user choice

            switch (choice) { // Handle user choice
                case "1": showNotes(); break; // Show notes
                case "2": addNote(notesFile); break; // Add a note
                case "3": deleteNote(notesFile); break; // Delete a note
                case "4": exitApp(); return; // Exit the application
                default: System.out.println("Invalid choice. Please select 1, 2, 3 or 4."); // Invalid choice message
            }
        }
    }

    private static void showMenu() {
        System.out.println("\nChoose an option:"); // Menu header
        System.out.println("1. Show notes");
        System.out.println("2. Add a note");
        System.out.println("3. Delete a note");
        System.out.println("4. Exit");
    }

    /**
     * Displays all notes. If no notes are available, informs the user.
     */
    private static void showNotes() {
        if (notes.isEmpty()) { // Check if there are no notes
            System.out.println("No notes available."); // Message if no notes
        } else {
            System.out.println("Your notes:\n"); // Header for notes
            for (int i = 0; i < notes.size(); i++) { // Loop through notes
                Note note = notes.get(i); // Get the note
                System.out.printf("%03d - %s: %s\n", i + 1, note.getTitle(), note.getContent()); // Print the note
            }
        }
    }


    /**
     * Adds a new note after prompting for title and content. Saves the note to file.
     */
    private static void addNote(File notesFile) {
        System.out.print("Enter a title for your note: "); // Prompt for title
        String title = scanner.nextLine().trim(); // Read title
        System.out.print("Enter your note: "); // Prompt for content
        String content = scanner.nextLine().trim(); // Read content

        if (content.isEmpty()) { // Check if content is empty
            System.out.println("Error: Note cannot be empty.\n"); // Error message
            return;
        }

        Note newNote = new Note(title, content); // Create a new note
        notes.add(newNote); // Add the note to the list
        saveNoteToFile(notesFile, newNote); // Save the note to the file
        System.out.println("Note added: " + title); // Confirmation message
    }

    /**
     * Deletes a note based on user input. Rewrites the notes file after deletion.
     */
    private static void deleteNote(File notesFile) {
        if (notes.isEmpty()) { // Check if there are no notes
            System.out.println("No notes to delete."); // Message if no notes
            return;
        }

        showNotes(); // Show existing notes
        System.out.print("Enter the number of the note to delete (or 0 to cancel): "); // Prompt for note number
        try {
            int id = Integer.parseInt(scanner.nextLine().trim()); // Read note number

            if (id == 0) { // Check if user wants to cancel
                System.out.println("Delete cancelled."); // Cancellation message
            } else if (id > 0 && id <= notes.size()) { // Check if valid note number
                Note removedNote = notes.remove(id - 1); // Remove the note
                rewriteNotesFile(notesFile); // Rewrite the notes file
                System.out.println("Deleted note: " + removedNote.getTitle()); // Confirmation message
            } else {
                System.out.println("Invalid note number."); // Invalid note number message
            }
        } catch (NumberFormatException e) { // Handle invalid input
            System.out.println("Invalid note number."); // Invalid input message
        }
    }

    /**
     * Reads notes from a file, parsing each line into a Note object. Returns a list of notes.
     * Handles file reading errors gracefully and ensures the file is closed after reading.
     */
    private static ArrayList<Note> readNotesFromFile(File file) {
        ArrayList<Note> notesList = new ArrayList<>(); // List to store notes
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) { // Open file for reading
            String line;
            while ((line = reader.readLine()) != null) { // Read each line
                String[] parts = line.split(":", 2); // Split line into title and content
                if (parts.length == 2) { // Check if split was successful
                    notesList.add(new Note(parts[0], parts[1])); // Create and add a new note
                }
            }
        } catch (IOException e) { // Handle file reading errors
            System.out.println("Couldn't read the notes file."); // Error message
        }
        return notesList; // Return the list of notes
    }

    /**
     * Rewrites the entire notes file with the current list of notes.
     */
    private static void rewriteNotesFile(File file) {
        try (FileWriter writer = new FileWriter(file, false)) { // Open file for writing
            for (Note note : notes) { // Loop through notes
                writer.write(note.getTitle() + ":" + note.getContent() + "\n"); // Write each note
            }
        } catch (IOException e) { // Handle file writing errors
            System.out.println("Error saving notes to file."); // Error message
        }
    }

    /**
     * Appends a single note to the notes file.
     */
    private static void saveNoteToFile(File file, Note note) {
        try (FileWriter writer = new FileWriter(file, true)) { // Open file for appending
            writer.write(note.getTitle() + ":" + note.getContent() + "\n"); // Write the note
        } catch (IOException e) { // Handle file writing errors
            System.out.println("Error saving note to file."); // Error message
        }
    }

    /**
     * Ensures the notes file exists, creating it and necessary directories if not.
     * Returns true if the file is ready for use, false if creation fails.
     */
    private static boolean setupNotesFile(File file) {
        if (file.exists()) { // Check if file already exists
            return true;
        }
        try {
            Files.createDirectories(Path.of(NOTES_PATH)); // Create necessary directories
            if (file.createNewFile()) { // Create the file
                System.out.println("Notes file created."); // Confirmation message
            } else {
                System.out.println("Error creating notes file."); // Error message
                return false;
            }
        } catch (IOException e) { // Handle file creation errors
            return false;
        }
        return true;
    }

    private static void exitApp() {
        System.out.println("Bye! See you next time."); // Goodbye message
        scanner.close(); // Close the scanner
    }

    private static void printUsage() {
        System.out.println("How to use this app:"); // Usage header
        System.out.println("java NotesApp [COLLECTION]"); // Command to run the app
        System.out.println("\nThis tool allows users to manage short single-line notes within a collection."); // Description
        System.out.println("\nPlease open README.md file for more information"); // Additional information
        System.out.println("\nOptions:"); // Options header
        System.out.println("-h, --help       Show this message"); // Help option
        System.out.println("[COLLECTION]     Name of your notes collection"); // Collection name option
    }

    public static String sanitizeFileName(String input) {
        String forbiddenChars = "[<>:\"/\\\\|?*\\x00-\\x1F]"; // Forbidden characters for Windows and Linux
        String cleanInput =  input.replaceAll(forbiddenChars, "").trim();
        if (cleanInput.isEmpty()) {
            System.out.println("Warning: input contained forbidden characters for filename");
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd_HHmmss");
            String placeholder = now.format(formatter);
            System.out.println("Warning: using collection name: "+placeholder);
            return placeholder;

        }
        return cleanInput;
    }

    private static class Note {
        private final String title; // Title of the note
        private final String content; // Content of the note

        public Note(String title, String content) {
            this.title = title.isEmpty() ? "(untitled)" : title; // Set title, default to "(untitled)" if empty
            this.content = content; // Set content
        }

        public String getTitle() {
            return title; // Getter for title
        }

        public String getContent() {
            return content; // Getter for content
        }
    }
}