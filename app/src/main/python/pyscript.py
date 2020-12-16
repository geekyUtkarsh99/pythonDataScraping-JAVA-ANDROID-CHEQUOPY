from bs4 import BeautifulSoup
from urllib.request import urlopen as uReq

url = "https://www.espn.com/nfl/qbr"
uClient = uReq(url)
req = uClient.read()
uClient.close()
soup = BeautifulSoup(req,"html.parser")

playername = [None,None,None]
paa =[]
x = None
y = None
z = None
qbr = [None,None,None]
plays = []


def return_player1():
  playername[0] = soup.find("tr",attrs={"data-idx": "0"}).text.replace(str(1),"")
  return playername[0]

def return_player2():
  playername[1] = soup.find("tr",attrs={"data-idx": "1"}).text.replace(str(2),"")
  return playername[1]

def return_player3():
  playername[2] = soup.find("tr",attrs={"data-idx": "2"}).text.replace(str(3),"")
  return playername[2]

def qbr1():
    for i in soup.findAll("div", attrs={"class": "Table__Scroller"}):
        qbr = i.findAll("tr", attrs={"data-idx": "0"})
        x = qbr[0].find("td", attrs={"class": "Table__TD"})
        return x.text


def qbr2():
 for i in soup.findAll("div", attrs={"class": "Table__Scroller"}):
  qbr = i.findAll("tr", attrs={"data-idx": "1"})
  x = qbr[0].find("td", attrs={"class": "Table__TD"})
  return x.text


def qbr3():
 for i in soup.findAll("div", attrs={"class": "Table__Scroller"}):
  qbr = i.findAll("tr", attrs={"data-idx": "2"})
  x = qbr[0].find("td", attrs={"class": "Table__TD"})
  return x.text




