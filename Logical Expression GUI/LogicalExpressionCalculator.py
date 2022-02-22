class ExpressionCalculator:
    def calculate_expression(dictionary, express):
        dict = dictionary.copy()
        expression = express

        opperators = ["and", "or", "n"]
        final_bool = False
        modifier = "or"

        if expression == "":
            return ("Invalid input")
        else:        
            inp = expression.split()

        #make na, nb, etc
        le = dict.copy()
        for i in le:
            dict['n' + i] = not dict[i]
        del le

        #calculates entry and validity of entry
        for i in range(len(inp)):
            if i % 2 == 0 and inp[i] in dict:
                if modifier == "and":
                    final_bool = final_bool and dict[inp[i]]
                elif modifier == "or":
                    final_bool = final_bool or dict[inp[i]]

                # adds (true) and (false) to expression for readability
                inp[i] = inp[i] + "(" + str(dict[inp[i]]) + ")"                     
            elif i % 2 != 0 and inp[i] in opperators:
                modifier = inp[i]
            else:
                return ("Invalid input")

            #writes inp back int expression with added (true) and (false)
            expression = " "
            for element in inp:
                expression = expression + element + " "

        return "value for \"" + expression + "\" is " + str(final_bool)