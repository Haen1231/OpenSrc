# -*- coding: utf-8 -*-

if __name__ == '__main__':
    h=456/60
    m=456%60
    print(str(int(h))+" : "+str(m))

    scores = {
        "홍길동":3.4,
        "임꺽정":4.0,
        "김철수":2.8,
        "이영희":3.9
    }
    data=scores.keys()
    data_list=list(data)
    data_list.sort()
    print(data_list)

    key=data_list[0]
    value=scores[key]
    print(key,value)
