import requests
from bs4 import BeautifulSoup

url_input = "https://catalog.emich.edu/preview_program.php?catoid=36&poid=15337"


r = requests.get(url_input)
soup = BeautifulSoup(r.content, "html.parser")
file =  open("test_file.txt", "w", encoding="utf-8")

prev_five = []
start_writing = False
for data in soup.find_all(["h2", "h3", "h4", "li", "p"]):
    sum = data.get_text()

    if "Critical Graduation Information" in sum:
        break
    if "Major Requirements:" in sum:
        start_writing = True

    repeated = False
    for i in prev_five:
        if sum in i:
            repeated = True
    if start_writing == True and repeated == False:
        file.writelines(sum + '\n')
    
    prev_five.append(sum)
    if len(prev_five) > 5:
        prev_five.remove(prev_five[0])

file.close()