import javafx.application.Application
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.layout.*
import javafx.stage.Stage
import java.awt.event.MouseEvent

class Main : Application()  {

    override fun start(stage: Stage) {

        // General note structure to represent any note
        class Note(var title: String, var content: String, var important: Boolean, var number: Int,
                   var msg: String = "")

        // global counter to keep track how many notes have been added
        var noteNumber: Int = 0

        // list of all the notes we have
        var notes = mutableListOf<Note>()
        // list of notes currently being displayed
        var displayingNotes = mutableListOf<Note>()

        //if the important filter is on
        var isImportantOn: Boolean = false
        // if the search filter is on
        var isSearchOn: Boolean = false

        // the note currently selected
        var selectedNote: Note = Note("", "", false, -1, "")

        // what is being searched
        var searchContent: String = ""

        // list used to generate Lorem Ipsum
        val loremIpsum: List<String> = listOf("sorem", "ipsum", "dolor", "sit", "amet", "consectetur", "adipiscing",
            "elit", "Mauris", "faucibus", "ultrices", "nunc", "rutrum", "sodales", "lorem", "bibendum", "vel",
            "saecenas", "sed", "dui", "pulvinar", "tincidunt", "eros", "vel", "varius", "nulla", "sed", "lacinia",
            "posuere", "rhoncus", "fusce", "sit", "amet", "nibh", "in", "sem", "faucibus", "vestibulum", "ac", "ut",
            "nibh", "integer", "placerat", "augue", "consequat", "mattis", "imperdiet", "sed", "pellentesque", "massa",
            "at", "velit", "gravida", "ac", "condimentum", "magna", "sagittis", "maecenas", "tempus", "mattis", "dui",
            "sed", "vehicula", "urna", "feugiat", "rhoncus", "aliquam", "erat", "volutpat", "donec", "semper", "quam",
            "vitae", "efficitur", "lacinia", "imperdiet", "purus", "morbi", "elit", "enim", "varius", "ac", "hendrerit",
            "eget", "lacinia", "at", "erat", "elementum", "lectus", "scelerisque", "viverra", "dignissim", "turpis",
            "ligula", "ultrices", "ligula", "id", "vehicula", "magna", "urna", "vel", "elit")


        // stack pane for adding or modifying note
        val layout = StackPane()

        // toolbar
        val toolBar = ToolBar()
        val addBtn = Button("Add")
        addBtn.prefWidth = 100.0
        addBtn.maxWidth = 100.0

        val randomBtn = Button("Random")
        randomBtn.prefWidth = 100.0
        randomBtn.maxWidth = 100.0
        val deleteBtn = Button("Delete")
        deleteBtn.prefWidth = 100.0
        deleteBtn.maxWidth = 100.0
        deleteBtn.setDisable(true);
        val clearBtn = Button("Clear")
        clearBtn.prefWidth = 100.0
        clearBtn.maxWidth = 100.0
        clearBtn.setDisable(true);
        val importantBtn = ToggleButton("!")

        val searchField = TextField(searchContent)
        toolBar.items.addAll(addBtn, randomBtn,
            deleteBtn, clearBtn, importantBtn, searchField)


        // scroll feature for all notes
        val scrollPane = ScrollPane()
        val borderPane = BorderPane()
        // disable horizontal scrolling
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER)

        // tile pane to display notes
        val notesPane = TilePane()
        notesPane.hgap = 5.0
        notesPane.vgap = 5.0
        notesPane.padding = Insets(10.0)
        scrollPane.content = notesPane

        // set up data and status line at the bottom
        var selectedNumberText: String = ""
        var statusNumberText: String = "0"
        var statusActionText: String = ""

        val statusLine = HBox()
        val selectedNumber = Label(selectedNumberText)
        val statusNumber = Label(statusNumberText)
        val statusAction = Label(statusActionText)
        statusLine.children.addAll(selectedNumber, statusNumber, statusAction)
        statusNumber.padding = Insets(0.0, 15.0, 0.0, 0.0)
        statusLine.prefHeight = 25.0
        statusLine.padding = Insets(10.0)


        // fill in content for the border pane (toolbar, notes pane and status line)
        borderPane.top = toolBar
        borderPane.center = scrollPane
        borderPane.bottom = statusLine


        layout.getChildren().add(borderPane)

        // a pane to create dim effect when adding or editing notes
        val dim = VBox()
        dim.background = Background(BackgroundFill(javafx.scene.paint.Color.DARKGREY,
            CornerRadii.EMPTY, Insets.EMPTY))
        layout.getChildren().add(dim)

        // dim feature is disabled by default
        dim.isVisible = false
        dim.opacity = 0.6

        // add new note/ edit note box
        val popup = VBox()
        popup.alignment = Pos.BASELINE_LEFT
        popup.maxWidth = 250.0
        popup.maxHeight = 200.0

        popup.setBorder(Border(
            BorderStroke(javafx.scene.paint.Color.DIMGREY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
                BorderWidths.DEFAULT)))
        popup.background = Background(BackgroundFill(javafx.scene.paint.Color.DARKGREY,
            CornerRadii.EMPTY, Insets.EMPTY))

        // set up data and UI for the pop to add/edit notes
        var headerText: String = ""
        var newTitleText: String = ""
        var newBodyText: String = ""
        var newIsImportnat: Boolean = false

        val header = Label(headerText)

        val titleRow = HBox()
        titleRow.alignment = Pos.BASELINE_LEFT
        val titleLabel = Label("Title")
        titleLabel.prefWidth = 30.0
        val titleField = TextField(newTitleText)
        titleField.prefWidth = 190.0
        titleRow.children.addAll(titleLabel, titleField)
        titleRow.spacing = 10.0

        val bodyRow = HBox()
        bodyRow.alignment = Pos.BASELINE_LEFT
        val bodyLabel = Label("Body")
        bodyLabel.prefWidth = 30.0
        val bodyField = TextField(newBodyText)

        bodyField.prefHeight = 200.0
        bodyField.prefWidth = 190.0
        bodyField.alignment = Pos.TOP_LEFT

        bodyRow.children.addAll(bodyLabel, bodyField)
        bodyRow.spacing = 10.0

        val importantRow = HBox()
        val importantLabel = Label("")
        importantLabel.prefWidth = 30.0
        val importantCheckbox = CheckBox("Important")
        importantCheckbox.isSelected = newIsImportnat
        importantRow.children.addAll(importantLabel, importantCheckbox)
        importantRow.spacing = 10.0


        val btnsRow = HBox()
        val saveBtn = Button("Save")
        val cancelBtn = Button("Cancel")
        btnsRow.children.addAll(saveBtn, cancelBtn)
        btnsRow.setAlignment(Pos.BASELINE_RIGHT)
        btnsRow.spacing = 10.0

        popup.getChildren().addAll(header, titleRow, bodyRow, importantRow, btnsRow)

        popup.alignment = Pos.TOP_LEFT

        popup.padding = Insets(10.0)
        popup.spacing = 10.0

        // pop up is not shown by default
        popup.setVisible(false)

        layout.getChildren().add(popup)

        // function to add a new note (bring up the right UI)
        fun addNewNote() {
            header.text = "Add New Note"
            popup.setVisible(true)
            dim.isVisible = true

        }

        // every time changes are being made to notes or displaying notes, we need to refresh the UI accordingly
        fun refreshNotes(notes: List<Note>, notesPane: TilePane) {
            notesPane.getChildren().clear()
            for (note in notes) {
                val noteVBox = VBox()
                noteVBox.setPrefSize(150.0, 200.0)

                // if a note is important, give it a yellow background, otherwise white
                if (note.important) {
                    noteVBox.background = Background(BackgroundFill(javafx.scene.paint.Color.LIGHTYELLOW,
                        CornerRadii.EMPTY, Insets.EMPTY))
                } else {
                    noteVBox.background = Background(BackgroundFill(javafx.scene.paint.Color.WHITE,
                        CornerRadii.EMPTY, Insets.EMPTY))
                }

                val noteTitleLabel = Label(note.title)
                val noteBodyLabel = Label(note.content)
                noteBodyLabel.maxHeight = 160.0
                noteBodyLabel.isWrapText = true
                noteVBox.children.addAll(noteTitleLabel, noteBodyLabel)
                noteVBox.spacing = 10.0
                noteVBox.padding = Insets(10.0)
                noteVBox.alignment = Pos.TOP_LEFT

                // if a note is selected, give it a highlighted border
                if (note == selectedNote) {
                    noteVBox.setBorder(Border(
                        BorderStroke(javafx.scene.paint.Color.BLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
                            BorderWidths.DEFAULT)))
                }

                // when is note is selected
                noteVBox.setOnMouseClicked {
                    selectedNote = note
                    refreshNotes(notes, notesPane)

                    // update the status bar at the bottom
                    selectedNumber.text = "#${selectedNote.number} | "

                    // when a note is double clicked, go into edit note mode
                    if (it.getClickCount() == 2) {
                        header.text = "Edit Note #${note.number}"
                        titleField.text = note.title
                        bodyField.text = note.content
                        importantCheckbox.isSelected = note.important
                        popup.setVisible(true)
                        dim.isVisible = true
                    }
                }
                notesPane.getChildren().add(noteVBox)
            }

            // clear button is only available when there is at least one note displayed
            if (notes.size > 0) {
                clearBtn.setDisable(false)
            }

            // delete button is only available when a note is currently selected
            if (selectedNote.number != -1) {
                deleteBtn.setDisable(false)
            } else {
                deleteBtn.setDisable(true)
            }

        }

        // capitalize a string
        fun capitalize(word: String): String {
            val result: String = word.take(1).uppercase() + word.drop(1).lowercase()
            return result
        }

        // use the Lorem Ipsum list to generate a new randomized note
        fun generateRandomNote() : Note {
            val note = Note("", "", false, noteNumber, "")

            var title: String = ""
            var content: String = ""
            var isImportant: Boolean = false

            // randomize sentence, ect number given in the assignment notes
            val titleNumber: Int = (1..3).random()
            val sentenceNumber: Int = (2..5).random()
            val importantNumber: Int = (1..5).random()

            // generate a random title
            for(i in 1..titleNumber) {
                val loremNum:Int = (0..101).random()
                val lorem: String = capitalize(loremIpsum[loremNum])
                title += lorem
                title += " "
            }

            // for each sentence, generate corresponding number of words
            for(i in 1..sentenceNumber) {
                val wordNumber:Int = (3..10).random()
                val loremNum:Int = (0..101).random()
                val firstWord: String = capitalize(loremIpsum[loremNum])
                content += firstWord
                for (j in 1..wordNumber-1) {
                    val newLoremNum:Int = (0..101).random()
                    content += loremIpsum[newLoremNum]
                    if (j == wordNumber-1) {
                        content += ". "
                    } else {
                        content += " "
                    }

                }
            }

            // if the the new random note is supposed to be important (1 in 5 chance)
            if (importantNumber == 1) {
                isImportant = true
            }

            note.title = title
            note.content = content
            note.important = isImportant

            // increment note number
            noteNumber += 1

            return note
        }

        // have a listener for the search field, update displaying notes accordingly
        searchField.setOnKeyTyped {
            searchContent = searchField.text.lowercase()

            // clear screen first
            displayingNotes.clear()

            // dectect if the important filter is on
            for (note in notes) {
                if (isImportantOn) {
                    if (note.important &&
                        (note.title.lowercase().contains(searchContent) || note.content.lowercase().contains(searchContent))) {
                        displayingNotes.add(note)
                    }
                } else {
                    if (note.title.lowercase().contains(searchContent) || note.content.lowercase().contains(searchContent)) {
                        displayingNotes.add(note)
                    }
                }
            }

            // if we are not searching for anything, return status bar back to normal
            if(searchContent == "") {
                statusNumber.text = "${notes.size} "
            } else {
                statusNumber.text = "${displayingNotes.size} (of ${notes.size}) "
            }

            // update UI
            refreshNotes(displayingNotes, notesPane)
        }

        // delete a selected note
        fun deleteNote(target: Note, notes: MutableList<Note>) {
            for (i in 0 until notes.size) {
                if (notes[i].number == target.number) {
                    notes.removeAt(i)
                    break
                }
            }
        }

        // clear all the notes currently being displayed
        fun clearNotes() {
            for (note in displayingNotes) {
                if (note.number == selectedNote.number) {
                    selectedNote.number = -1
                }
                deleteNote(note, notes)
            }

            statusAction.text = "Clear ${displayingNotes.size} Notes"
            displayingNotes.clear()
        }

        // add button function
        addBtn.setOnAction {
            addNewNote()
        }

        // random button function
        randomBtn.setOnAction {
            val randomNote = generateRandomNote()

            notes.add(randomNote)

            // decide if the new random should be displayed based on active filters
            if (isSearchOn && isImportantOn) {
                if (randomNote.important &&
                    (randomNote.title.contains(searchContent) || randomNote.content.contains(searchContent))) {
                    displayingNotes.add(randomNote)
                }
                statusNumber.text = "${displayingNotes.size} (of ${notes.size}) "
            } else if (isSearchOn) {
                if (randomNote.title.contains(searchContent) || randomNote.content.contains(searchContent)) {
                    displayingNotes.add(randomNote)
                }
                statusNumber.text = "${displayingNotes.size} (of ${notes.size}) "
            } else if (isImportantOn) {
                if (randomNote.important) {
                    displayingNotes.add(randomNote)
                }
                statusNumber.text = "${displayingNotes.size} (of ${notes.size}) "
            } else {
                displayingNotes.add(randomNote)
                statusNumber.text = "${notes.size} "
            }

            // update status bar
            statusAction.text = "Added Note #${randomNote.number} "

            // update UI
            refreshNotes(displayingNotes, notesPane)
        }

        // delete button function
        deleteBtn.setOnAction {
            // update status bar
            statusAction.text = "Deleted Note #${selectedNote.number}"
            selectedNumber.text = ""

            // delete selected note
            deleteNote(selectedNote, notes)
            deleteNote(selectedNote, displayingNotes)

            // disable delete btn
            selectedNote.number = -1

            // update status bar
            if (isSearchOn || isImportantOn) {
                statusNumber.text = "${displayingNotes.size} (of ${notes.size}) "
            } else {
                statusNumber.text = "${notes.size}"
            }

            // refresh UI
            refreshNotes(notes, notesPane)
        }

        // clear btn function
        clearBtn.setOnAction {
            clearNotes()

            // update UI
            refreshNotes(notes, notesPane)
            statusNumber.text = "${notes.size} "
        }

        // save btn function
        saveBtn.setOnAction {
            // get values from the pop up window
            val titleText: String = titleField.getText()
            val bodyText: String = bodyField.getText()
            val isImportant:Boolean = importantCheckbox.isSelected()
            val newNote = Note(titleText, bodyText, isImportant, noteNumber, "")

            // if we are adding a new note
            if (header.text == "Add New Note") {
                // add note and increment number
                notes.add(newNote)
                noteNumber += 1

                // decide if the new note will be displayed based on active filters
                if (isSearchOn && isImportantOn) {
                    if (newNote.important &&
                        (newNote.title.contains(searchContent) || newNote.content.contains(searchContent))) {
                        displayingNotes.add(newNote)
                    }
                    statusNumber.text = "${displayingNotes.size} (of ${notes.size}) "
                } else if (isSearchOn) {
                    if (newNote.title.contains(searchContent) || newNote.content.contains(searchContent)) {
                        displayingNotes.add(newNote)
                    }
                    statusNumber.text = "${displayingNotes.size} (of ${notes.size}) "
                } else if (isImportantOn) {
                    if (newNote.important) {
                        displayingNotes.add(newNote)
                    }
                    statusNumber.text = "${displayingNotes.size} (of ${notes.size}) "
                } else {
                    displayingNotes.add(newNote)
                    statusNumber.text = " ${notes.size} "
                }

                // update status bar
                statusAction.text = "Add Note #${newNote.number}"

                // if we are editing an existing note
            } else {
                // update that note's content
                selectedNote.title = titleText
                selectedNote.content = bodyText
                selectedNote.important = isImportant
                statusAction.text = "Edited Note #${selectedNote.number}"
            }

            // refresh UI
            refreshNotes(displayingNotes, notesPane)

            // disable pop up window
            popup.isVisible = false
            dim.isVisible = false
        }

        // simply disable pop up when clicked
        cancelBtn.setOnAction {
            popup.isVisible = false
            dim.isVisible = false
        }

        // important filter
        importantBtn.setOnAction {
            // toggle filter value
            isImportantOn = !isImportantOn

            // if filter is active
            if (isImportantOn) {
                // clear and update notes being displayed
                displayingNotes.clear()

                for (note in notes) {
                    if (note.important) {
                        displayingNotes.add(note)
                    }
                }
            } else {
                displayingNotes.clear()
                for (note in notes) {
                    displayingNotes.add(note)
                }
            }

            // update status bar
            if (isImportantOn) {
                statusNumber.text = "${displayingNotes.size} (of ${notes.size}) "
            } else {
                statusNumber.text = "${notes.size} "
            }

            refreshNotes(displayingNotes, notesPane)
        }

        // build the scene graph
        // create and show the scene
        val scene = Scene(layout)
        scene.root.style = "-fx-font-family: 'sans-serif'"
        stage.title = "A1 notes (z995li)"
        stage.width = 800.0
        stage.height = 600.0
        stage.minWidth = 400.0
        stage.minHeight = 400.0
        stage.scene = scene
        stage.show()
    }
}