package com.softcare.raphnote.model;

public interface ClickObserver {
       void click(long id);
       void click(long id, Long time, String text);
}
/*
Note edit app simple and secured for keeping text notes. It have the capability of exporting your text to file to view on computer.
Apart provide a good interface for typing it also gives you ability open files that it file type is not specified as text file to see it raw content.
The app is aim at speed for manipulating simple note.
How to use the app
The app have four main views
Notes lists (initial view)
View/preview  note0
Create/edit note
Settings

Now open the app

Functions available Note list view
       Press on add button to create new note(bottom center).
       Press on a note to view/edit (take you to view/preview where you can edit by pressing the edit icon)
       Press search icon(top right second icon) then type to search for notes(use to find note when notes list is much.
       Press options menu icon(top right first icon) then press settings(moving to settings view)

Functions available Note create/edit
       press the check to preview
       press back arrow to end edit

Functions available in preview/view note.
       press edit ico in preview to edit note(bottom center)
       Press search icon(top right second icon) then type to search for a word or sentence occurrence in the note.
       Press options menu icon(top right first icon) then press:
              Export (to save note as text file)
              copy all (to copy note to clipboard so as to past it some where)
              share ( to share text with people using apps(like Message app, WhatApp app, Facebook, Email,e.t.c)
              delete(to completely remove the note)

Functions available in Settings
       select note list ordering (Order by: Ascending or Descending)
       select note list sorting(Sort by date created or modified)
       select security check/time(Require system password after 1 minute, 2 minute, 3 minute, 5 minutes or none)





 */
