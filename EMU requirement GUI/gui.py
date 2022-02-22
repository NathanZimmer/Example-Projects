import tkinter as tk
from tkinter import StringVar
import os
from functools import partial
#from html_to_file import WebsiteToFile
from read_file import FileToGUI


class GUI:
    def __init__(self, master):
        self.master = master

        windowHeight = int(master.winfo_screenheight()/8)
        windowWidth = int(master.winfo_screenwidth()/7)
        master.geometry("1280x720+{}+{}".format(windowWidth, windowHeight))
        #master.minsize(600, 800)

        if os.path.getsize("test_file.txt") == 0:
            self._input_window()
        else:
            self._set_up_ui()

    #creates a window for url input. called when application is opened for the first time
    def _input_window(self):
        self.url = StringVar() # variable for url input

        window = tk.Toplevel(self.master)
        window.attributes("-topmost", True)
        window.title("URL input")

        label = tk.Label(window, text = "Enter URL for your Major's web catalog:")
        url_entry = tk.Entry(window, textvariable = str(self.url))
        button = tk.Button(window, text = "Enter", command = partial(self._url_to_file, self.url, window))

        # layout
        label.grid(row = 0, column = 0, pady = 5, padx = 5, sticky = "w")
        url_entry.grid(row = 1, column = 0, padx = 6, ipadx = 150)
        button.grid(row = 1, column = 2)

        windowHeight = int(self.master.winfo_screenheight()/3)
        windowWidth = int(self.master.winfo_screenwidth()/2.75)
        window.geometry("480x80+{}+{}".format(windowWidth, windowHeight))
        window.resizable(False, False)

    #local call to html_to_file's url_to_file method
    def _url_to_file(self, url, window):
        #WebsiteToFile.to_file(url, window)

        if os.path.getsize("test_file.txt") > 0:
            self._set_up_ui()

    #sets up ui for main window based off of file
    def _set_up_ui(self):
        FileToGUI.to_gui(self, self.master, tk, "")


root = tk.Tk()
gui = GUI(root)
root.mainloop()

#need to write setup ui method