if __name__ == '__main__':
    print("실습 1")
    str="""   죽는 날까지 하늘을 우러러
    한 점 부끄럼이 없기를,
    잎새에 이는 바람에도
    나는 괴로워했다.
    별을 노래하는 마음으로
    모든 죽어가는 것을 사람해야지
    그리고 나한테 주어진 길을
    걸어가야겠다.
    오늘 밤에도 별이 바람에 스치운다.    """
    str=str.strip()
    str=str.replace("나","너")
    print(str)
    
    print("--------------------------------------")
    print("실습 2")
    str=input("Numbers=?")
    strlist=str.split()
    
    max=int(strlist[0])
    
    
    print("Numbers=? ")
    print(max)
    for str1 in strlist[1:]:
        num=int(str1)
        print(num)
        if(num>max):
            max = num
            
    print("Max=", max)
    
    print("--------------------------------------")
    print("실습 3")
    def conv_asc(str, num):
        new_str=""
        for i in str:
            code=ord(i)
            new_str += chr(code+num)
        return new_str
    
    def enc(str, key):
        list = str.split()
        list = list[::-1]
        new_str=" ".join(list)
        return conv_asc(new_str,key)
    
    def dec(str, key):
        str=conv_asc(str,key*-1)
        list = str.split()
        list = list[::-1]
        new_str=" ".join(list)
        return new_str
    
    str = "abc def"
    enc_str= enc(str,1)
    print("\""+str+"\"is encoded into \""+enc_str+"\"")
    
    dec_str= dec(enc_str,1)
    print("\""+enc_str+"\"is decoded into \""+dec_str+"\"")