import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

public class NotesApp {
    private static String collectionName;
    private static final String NOTES_PATH = System.getProperty("user.dir") + "/notes/";
    private static final String NOTES_FILE = "notes.txt";
    private static ArrayList<Note> notes = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        if (args.length != 1 || args[0].equals("-h") || args[0].equals("--help")) {
            printUsage();
            return;
        }

        collectionName = args[0];
        System.out.println("Welcome to the Notes App!\nCollection: " + collectionName);

        File notesFile = new File(NOTES_PATH + collectionName + "_" + NOTES_FILE);

        if (!setupNotesFile(notesFile)) {
            System.out.println("ERROR: Unable to initialize notes file.");
            return;
        }

        notes = readNotesFromFile(notesFile);

        while (true) {
            showMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1": showNotes(); break;
                case "2": addNote(notesFile); break;
                case "3": deleteNote(notesFile); break;
                case "4": exitApp(); return;
                default: System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void showMenu() {
        System.out.println("\nChoose an option:");
        System.out.println("1. Show notes");
        System.out.println("2. Add a note");
        System.out.println("3. Delete a note");
        System.out.println("4. Exit");
    }

    /**
     * Displays all notes. If no notes are available, informs the user.
     */
    private static void showNotes() {
        if (notes.isEmpty()) {
            System.out.println("No notes available.");
        } else {
            System.out.println("Your notes:\n");
            for (int i = 0; i < notes.size(); i++) {
                Note note = notes.get(i);
                System.out.printf("%03d - %s: %s\n", i + 1, note.getTitle(), note.getContent());
            }
        }
    }


    /**
     * Adds a new note after prompting for title and content. Saves the note to file.
     */
    private static void addNote(File notesFile) {
        System.out.print("Enter a title for your note: ");
        String title = scanner.nextLine().trim();
        System.out.print("Enter your note: ");
        String content = scanner.nextLine().trim();

        if (content.isEmpty()) {
            System.out.println("Error: Note cannot be empty.\n");
            return;
        }

        Note newNote = new Note(title, content);
        notes.add(newNote);
        saveNoteToFile(notesFile, newNote);
        System.out.println("Note added: " + title);
    }

    /**
     * Deletes a note based on user input. Rewrites the notes file after deletion.
     */
    private static void deleteNote(File notesFile) {
        if (notes.isEmpty()) {
            System.out.println("No notes to delete.");
            return;
        }

        showNotes();
        System.out.print("Enter the number of the note to delete (or 0 to cancel): ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());

            if (id == 0) {
                System.out.println("Delete cancelled.");
            } else if (id > 0 && id <= notes.size()) {
                Note removedNote = notes.remove(id - 1);
                rewriteNotesFile(notesFile);
                System.out.println("Deleted note: " + removedNote.getTitle());
            } else {
                System.out.println("Invalid note number.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid note number.");
        }
    }

    /**
     * Reads notes from a file, parsing each line into a Note object. Returns a list of notes.
     * Handles file reading errors gracefully and ensures the file is closed after reading.
     */
    private static ArrayList<Note> readNotesFromFile(File file) {
        ArrayList<Note> notesList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":", 2);
                if (parts.length == 2) {
                    notesList.add(new Note(parts[0], parts[1]));
                }
            }
        } catch (IOException e) {
            System.out.println("Couldn't read the notes file.");
        }
        return notesList;
    }

    /**
     * Rewrites the entire notes file with the current list of notes.
     */
    private static void rewriteNotesFile(File file) {
        try (FileWriter writer = new FileWriter(file, false)) {
            for (Note note : notes) {
                writer.write(note.getTitle() + ":" + note.getContent() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error saving notes to file.");
        }
    }

    /**
     * Appends a single note to the notes file.
     */
    private static void saveNoteToFile(File file, Note note) {
        try (FileWriter writer = new FileWriter(file, true)) {
            writer.write(note.getTitle() + ":" + note.getContent() + "\n");
        } catch (IOException e) {
            System.out.println("Error saving note to file.");
        }
    }

    /**
     * Ensures the notes file exists, creating it and necessary directories if not.
     * Returns true if the file is ready for use, false if creation fails.
     */
    private static boolean setupNotesFile(File file) {
        if (file.exists()) {
            return true;
        }
        try {
            Files.createDirectories(Path.of(NOTES_PATH));
            if (file.createNewFile()) {
                System.out.println("Notes file created.");
            } else {
                System.out.println("Error creating notes file.");
                return false;
            }
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    private static void exitApp() {
        System.out.println("Bye! See you next time.");
        scanner.close();
    }

    private static void printUsage() {
        System.out.println("How to use this app:");
        System.out.println("java NotesApp [COLLECTION]");
        System.out.println("\nOptions:");
        System.out.println("-h, --help       Show this message");
        System.out.println("[COLLECTION]     Name of your notes collection");
    }

    private static class Note {
        private final String title;
        private final String content;

        public Note(String title, String content) {
            this.title = title.isEmpty() ? "(untitled)" : title;
            this.content = content;
        }

        public String getTitle() {
            return title;
        }

        public String getContent() {
            return content;
        }
    }
}