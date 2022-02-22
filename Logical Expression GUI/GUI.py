from tkinter import *
from functools import partial
from LogicalExpressionCalculator import ExpressionCalculator

class GUI:
    def __init__(self, master):
        self.master = master
        master.title("Logical Expression Calculator")
        
        self.tf_value = [] # list that contains values for the true/false radio buttons
        self.var_num = StringVar() # the value of bool_num_entry

        label_1 = Label(master, text = "Number of variables \n(must be between 1 and 26): ") 
        bool_num_entry = Entry(master, textvariable = str(self.var_num))
        button_1 = Button(master, text = "Enter", command = self.set_and_display_vars)

        # layout 

        label_1.grid(row = 0, column = 0, padx = 4, pady = 10, columnspan = 4, sticky = "w")
        bool_num_entry.grid(row = 0, column = 4)
        button_1.grid(row = 0, column = 5, padx = 5)

        windowHeight = int(master.winfo_screenheight()/3)
        windowWidth = int(master.winfo_screenwidth()/2.5)
        master.geometry("350x200+{}+{}".format(windowWidth, windowHeight))
        master.minsize(350, 60)

    #sets number of variables, makes a dict containing said variables and "True", creates true and false radio buttons for each variable, and creates entry/label for entering logical expressio
    def set_and_display_vars(self):
        self.clear_grid(1)
        vars = self.var_num.get()
        
        #makes sure input is valid
        try:
            vars = int(vars)
            if vars < 1 or vars > 26:
                raise IndexError
        except:
            self.var_num.set("")
            return
        
        #fills dictionary and tf_values
        self.dict = {}
        for i in range(vars):
            value = (chr(97 + i))
            self.dict[value] = False
            self.tf_value.append(IntVar())

        #makes radio buttons and labels
        for i in self.dict.keys():
            frame = Frame(self.master)
            row_num = int(1 + list(self.dict.keys()).index(i))

            label_2 = Label(self.master, text = i + ": ")
            radioT = Radiobutton(frame, text = "False", variable = self.tf_value[row_num - 1], value = 0, command = partial(self.assign_tf_values, i, 0))
            radioT.pack(side = RIGHT)
            radioF = Radiobutton(frame, text = "True", variable = self.tf_value[row_num - 1], value = 1, command = partial(self.assign_tf_values, i, 1))
            radioF.pack(side = RIGHT)

            # layout

            frame.grid(row = row_num, column = 1, sticky = "w")
            label_2.grid(row = row_num, column = 0, sticky = "e")

        #makes label and entry for logical expression
        self.logical_expression = StringVar()

        label_3 =Label(self.master, text = "Enter logical expression \nto be calcuated: ")
        expression_entry = Entry(self.master, textvariable = self.logical_expression)
        button_2 = Button(self.master, text = "Enter", command = self.calculate_logical_expression)
        button_3 = Button(self.master, text = "Clear", command = partial(self.clear_grid, len(self.dict.keys()) + 2))

        # layout
        row_num = len(self.dict.keys()) + 1
        label_3.grid(row = row_num, column = 0, padx = 4,columnspan = 4, sticky = "w")
        expression_entry.grid(row = row_num, column = 4)
        button_2.grid(row = row_num, column = 5, padx = 5)
        button_3.grid(row = row_num, column = 6, padx = 5)
        self.answer_column = row_num + 1

        self.master.geometry("")

    #clears all grid values below the first row
    def clear_grid(self, row_num):
        for label in self.master.grid_slaves():
            if int(label.grid_info()["row"] > row_num):
                label.grid_forget()
        if row_num == 0:
            self.tf_value = []

    #assigns the tf values based on what radio button is clicked
    def assign_tf_values(self, i, index):

        if index == 0:
            self.dict[i] = False
        else:
            self.dict[i] = True

    def calculate_logical_expression(self):
        final_bool = ExpressionCalculator.calculate_expression(self.dict, self.logical_expression.get())
        self.logical_expression.set("")

        self.answer_column = self.answer_column + 1
        label_4 = Label(self.master, text = " - " + final_bool)
        label_4.grid(row = self.answer_column, column = 0, padx = 4, pady = 10, columnspan = 7, sticky = "w")
        

root = Tk()
gui = GUI(root)
root.mainloop()