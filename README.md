
# &#128221; NotesApp



<table>
  <tr>
    <td>
    <img  src="https://static.vecteezy.com/system/resources/previews/046/934/238/non_2x/blank-notebook-with-pen-on-transparent-background-free-png.png">
    </td>
    <td>
    <strong>NotesApp</strong> is an interactive command-line tool that allows users to manage short, single-line notes within named collections. Each collection is stored in a separate text file, ensuring data persistence between runs. The program enables users to view, add, and delete notes from a specified collection.
    </td>
  </tr>
</table>


---

## &#x1F680; Key Features

- **Interactive CLI menu** for easy note management  
- Each collection is stored in a **separate text file**  
- **Data persistence** across sessions  
- **Graceful error handling** to prevent crashes  
- **Help menu** for user guidance  

---

## &#x1F6E0;&#xFE0F; Installation Prerequisites

### Requirements:  
- **Visual Studio** app installed on your computer/laptop  
- **Java Development Kit (JDK)** installed (version 21 or later)  
- **Terminal** or **Command Prompt**  
- Java code with the program will be provided  

---

## &#x1F5A5;&#xFE0F; Usage and Example Commands

### Getting Started  
1. Open the provided Java code via the **Visual Studio** app.  
   *(Note: Java Development Kit (JDK) must be installed beforehand)*  
2. Perform compilation by running the following command in the terminal:  
   `javac NotesApp.java `
3. You are ready to run **NotesApp**!  


---

### Running the Application  

To start the application, provide the collection name as an argument:  

`java NotesApp [COLLECTION_NAME]`  

If the specified collection does not exist, it will be created automatically.  

---

### Available Commands  

#### Display Usage Information  

`java NotesApp --help `

**Output:**  
```
How to use this app:  
java NotesApp [COLLECTION]  

This tool allows users to manage short single-line notes within a collection.  

Please open README.md file for more information.  

Options:  
-h, --help       Show this message  
[COLLECTION]     Name of your notes collection  
```
---

### Example Session  
```
Welcome to the Notes App!  
Collection: my_notes  
Notes file created.  

Choose an option:  
1. Show notes  
2. Add a note  
3. Delete a note  
4. Exit  
1  
No notes available.  

Choose an option:  
1. Show notes  
2. Add a note  
3. Delete a note  
4. Exit  
5  
Invalid choice. Please try again.  

Choose an option:  
1. Show notes  
2. Add a note  
3. Delete a note  
4. Exit  
2  
Enter a title for your note: TEST_1 TEST_1 TEST_1  
Enter your note: Hello /kood!            
Note added: TEST_1 TEST_1 TEST_1  

Choose an option:  
1. Show notes  
2. Add a note  
3. Delete a note  
4. Exit  
1  
Your notes:  

001 - TEST_1 TEST_1 TEST_1: Hello /kood!  

Choose an option:  
1. Show notes  
2. Add a note  
3. Delete a note  
4. Exit  
2  
Enter a title for your note: TEST_2 TEST_2 TEST_2         
Enter your note: The Great GatSBY  
Note added: TEST_2 TEST_2 TEST_2  

Choose an option:  
1. Show notes  
2. Add a note  
3. Delete a note  
4. Exit  
4  
Bye! See you next time.  
```
---

## &#x2753; Support  

If you encounter any issues, please open an issue on the repository, describing the problem and the steps to reproduce it.

---

## &#x1F465; Contributors  

- **Tom Kristian Abel**  
- **Mykyta Kotliarchuk**  
- **Beno Kudrin**  

---


