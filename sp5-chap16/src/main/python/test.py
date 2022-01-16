from flask import Flask, render_template, request
import numpy as np
import pandas as pd
import pymysql
from numpy import dot
from numpy.linalg import norm
import matplotlib.pyplot as plt

# db접근
# database에 접근
db = pymysql.connect(host='localhost',
                     port=3306,
                     user='root',
                     passwd='dkcmrl1011',
                     db='foodvar',
                     charset='utf8')

app = Flask(__name__)

# 하루 영양
"""
남자 : 칼로리 2301, 단백질 84, 지방 56.8, 탄수화물 325, 
당 64.5, 나트륨 3809.5, 콜레스테롤 298.1, 포화지방 18.9, 트랜스지방 x

평균 1987.7, 72.4 49.5 288 60 3255 260.4 16.6
여자 1661.1 59.8 41.9 249 55.6 2680 221 14.2
"""

# 평균 요구치, 칼로리 단백질 지방 탄수화물 당 나트륨 콜레스테롤 포화지방 순
mean = np.array([1987.7, 72.4, 49.5, 288, 60, 3255])
mean *= 7
csv = pd.read_csv('totalfoodDBNoheader.csv') #, header=False
#csv = csv.loc[:, ['식품명', '에너지(㎉)', '단백질(g)', '지방(g)','탄수화물(g)','총당류(g)', '나트륨(㎎)']]
#csv.to_csv('totalfoodDBNoheader.csv', encoding="utf-8-sig")

recfoodlist = []

def cos_sim(idx, gap, gotfood):
    global recfoodlist
    #받은 데이터를, 숫자가 아닌 것들 0으로
    gotfood = gotfood[2:]
    for i in range(len(gotfood)):
        try:
            gotfood[i] = float(gotfood[i])
        except ValueError:
            gotfood[i] = 0
    #gotfoodnp = np.array([i for i in gotfood]) #print(gap)
    #print(gap.shape)   #print(gotfood)
    #print(gotfood.shape)
    normret = norm(gap) * norm(gotfood)
    dott = dot(gap, gotfood)
    if normret != 0:
        ret = dott/(normret)
    else:
        ret = 0

    if ret >= 0.998 :
        #결과값, 순서, 그 영양소 저장
        tmplist = [normret, idx]
        recfoodlist.append(tmplist)
        #int(idx)
    return None

#db내에서 음식 블랙리스트찾기
def findblacklist(foodlist, userId):
    retval = -1
    try:
        cursor = db.cursor()
        for normret, idx in foodlist:
            #현재 ID가 고른 블랙리스트에 해당하는 음식인가? 맞다면, PASS고 아니면 리턴
            cursor.execute('SELECT IDX FROM foodblacklist WHERE ID = %s AND IDX = %s limit 1', (idx, userId))
            result = cursor.fetchone()
            if result is not None:
                continue
            else:
                retval = idx
                break
    except Exception as e:
        print('db error:', e)
    finally:
        #db.close()
        if retval == -1:
            return None
        else:
            return idx

@app.route("/tospring", methods=['POST'])
def spring():
    global mean
    global recfoodlist

    content = request.json
    chole, kcal = content['chole'], content['kcal']
    prot, carbs = content['prot'], content['carbs']
    sugar, nat = content['sugar'], content['nat']
    satur, fat = content['satur'], content['fat']
    userId = content['userId']

    userstat = np.array([kcal, prot, fat, carbs, sugar, nat])
    gap = np.array(mean - userstat)
    print(gap)
    #print(csv[:1, :])
    #print(csv.iloc[[0]]) # 0~3번째까지이 행, 열은 전체
    for idx in csv.index: #csv.shape[0]
        #print(csv.loc[idx])
        cos_sim(idx, gap, csv.loc[idx])
        if idx == 100:
            break
    recfoodlist.sort(reverse=True)
    print(recfoodlist)
    #recfoodlist = np.array(recfoodlist)

    #블랙리스트 음식 아닌거 찾기, iloc함수이용
    finded = findblacklist(recfoodlist, userId)
    if finded is not None:
        ret = str(csv.iloc[[finded]]['식품명'])
        text = ret.split()
        ret = ""
        for i in range(1, len(text)):
            if(text[i] == "Name:"):
                break
            ret += text[i] + " "
    else:
        ret = "해당하는 음식이 없습니다"

    recfoodlist = []
    #return "{\"kcal\": " + str(gap[0]) + "," + "\"prot\": " + str(gap[1]) + "," + \
    #"\"fat\": " + str(gap[2]) + "," + "\"carbs\": " + str(gap[3]) + "," + \
    #"\"sugar\": " + str(gap[4]) + "," +  "\"nat\": " + str(gap[4]) + "," + \
    #"\"recfoodname\": " + ret + "}"
    return str(gap[0]) + " " + str(gap[1]) + " " + str(gap[2]) + " " + str(gap[3]) + \
        " " + str(gap[4]) + " " + str(gap[5]) + " " + ret

print("hi")
app.run()