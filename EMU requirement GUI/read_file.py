import collections
import re

class FileToGUI:

    def to_gui(self, master, tk, file):
        scrollbar = tk.Scrollbar(master)
        canvas = tk.Canvas(master, yscrollcommand = scrollbar.set)
        frame = tk.Frame(canvas)

        frame.bind(
            "<Configure>",
            lambda e: canvas.configure(
                scrollregion=canvas.bbox("all")
            )
        )

        canvas.create_window((0, 0), window=frame, anchor="nw")
        canvas.config(width = 400, height = 720)

        if file == "":
            file = open("test_file.txt", "r", encoding="utf-8")
            line = file.readline()

            prev = line[1]
            tk.Label(frame, text = line[3:]).grid(row = 0, column = 0, sticky = "w")

        row_num = 0 
        for line in file:
            if len(line) > 50:
                line = line[0:49] + "\n" + line[50:]

            row_num += 1
            if re.search("h[1-9]>", line):
                tk.Radiobutton(frame, text = line[3:], indicator = 0, background = "light blue").grid(row = row_num, column = 0, sticky = "w")
            elif re.search("([A-Z]{4}|[A-Z]{3})[ ]([0-9]{3}[A-Z]|[0-9]{3})", line) and re.search("[0-9] hr", line):
                tk.Label(frame, text = line).grid(row = row_num, column = 0, sticky = "w")
                tk.Radiobutton(frame, text = "Done", indicator = 0, background = "light blue").grid(row = row_num, column = 1, sticky = "w")

        canvas.pack(side = "left")
        scrollbar.pack(side = "left", fill = "y")
        scrollbar.config(command = canvas.yview)



    """
    def to_GUI(self, master, tk, file):
        frame = ScrollableFrame(master)

        if file == "":
            file = open("test_file.txt", "r", encoding="utf-8")
            line = file.readline()

            prev = line[1]
            ttk.Label(frame, text = line[3:]).pack(side = "top")

        row_num = 0 
        for line in file:
            row_num += 1
            if re.search("h[1-9]>", line):
                print("")
                #tk.Radiobutton(frame, text = line[3:], indicator = 0, background = "light blue").pack(side = "top")
            elif re.search("([A-Z]{4}|[A-Z]{3})[ ]([0-9]{3}[A-Z]|[0-9]{3})", line) and re.search("[0-9] hr", line):
                ttk.Label(frame, text = line).pack(side = "top")
                ttk.Button(frame, text = "Done").pack(side = "top")

        frame.pack()
        """

        








"""
for line in file:
    if pattern is class type- make it a label and add button for class is done and that kind of thing
    else if pattern is category- make it a button
        if category is > current category header, recursively call this funciton?
    else if pattern is description make it a label 

"""
