package com.example.searchgame2;


public class Translaytor {

    public String Translayt(String string){
        StringBuilder builder= new StringBuilder(string);
        String janre = "";
        for(int i=0;i<builder.length();i++){
            switch (builder.charAt(i)){
                case 'a':janre+='А';break;
                case 'b':janre+='Б';break;
                case 'c':try{if(builder.charAt(i+1)=='h'){janre+='Ч';i++;break;}else{janre+='Ц';break;}}catch(StringIndexOutOfBoundsException e)
                    {janre+='Ц';break;}
                case 's':try{if(builder.charAt(i+1)=='h'&&builder.charAt(i+2)=='h'){janre+='Щ';i++;i++;break;}if(builder.charAt(i+1)=='h')
                    {janre+='Ш';i++;break;}else{janre+='С';break;}}catch(StringIndexOutOfBoundsException e){janre+='С';break;}
                case 'y':try{if(builder.charAt(i+1)=='u'){janre+='Ю';i++;break;}if(builder.charAt(i+1)=='a'){janre+='Я';i++;break;}
                    if(builder.charAt(i+1)=='y'){janre+='И';janre+='Й';i++;break;}if(builder.charAt(i+1)=='o'){janre+='Й';janre+='О';i++;break;}
                    else{janre+='И';break;}}catch(StringIndexOutOfBoundsException e){janre+='И';break;}
                case 'z':try{if(builder.charAt(i+1)=='h'){janre+='Ж';i++;break;}else{janre+='З';}break;}catch(StringIndexOutOfBoundsException e)
                    {janre+='З';break;}
                case '-':janre+='Ь';break;
                case 'v':janre+='В';break;
                case 'g':janre+='Г';break;
                case 'h':janre+='Г';break;
                case 'd':janre+='Д';break;
                case 'j':janre+='Й';break;
                case 'k':janre+='К';break;
                case 'l':janre+='Л';break;
                case 'm':janre+='М';break;
                case 'n':janre+='Н';break;
                case 'p':janre+='П';break;
                case 'r':janre+='Р';break;
                case 't':janre+='Т';break;
                case 'u':janre+='У';break;
                case 'f':janre+='Ф';break;
                case 'o':janre+='O';break;
                case 'i':janre+='І';break;
                case 'e':janre+='Е';break;
                case '_':janre+=' ';break;
                default:janre+=builder.charAt(i);break;
            }
        }
        return janre;
    }
    public String Breaker (String s, String between){
        String result = "";
        StringBuilder s1 = new StringBuilder(s);
        for(int i = 0; i < s1.length(); i++){
            switch(s1.charAt(i)){
                case '-':
                    if(s1.charAt(i+1)=='-'){result+=' ';i++;break;}else{result+=" "+between+" ";break;}
                case '_':result+='.';break;
                case 'g':result+='G';break;
                case 'h':result+='H';break;
                case 'z':result+='Z';break;
                default:result+=s1.charAt(i);break;
            }
        }
     return result;
    }
    public String gup(String s){
        String result = " ";
        StringBuilder sb = new StringBuilder(s);
        for(int i = 0; i < sb.length(); i++){
            switch(sb.charAt(i)){
                case 'q':result+='Q';break;
                case 'w':result+='W';break;
                case 'e':result+='E';break;
                case 'r':result+='R';break;
                case 't':result+='T';break;
                case 'y':result+='Y';break;
                case 'u':result+='U';break;
                case 'i':result+='I';break;
                case 'o':result+='O';break;
                case 'p':result+='P';break;
                case 'a':result+='A';break;
                case 's':result+='S';break;
                case 'd':result+='D';break;
                case 'f':result+='F';break;
                case 'g':result+='G';break;
                case 'h':result+='H';break;
                case 'j':result+='J';break;
                case 'k':result+='K';break;
                case 'l':result+='L';break;
                case 'z':result+='Z';break;
                case 'x':result+='X';break;
                case 'c':result+='C';break;
                case 'v':result+='V';break;
                case 'b':result+='B';break;
                case 'n':result+='N';break;
                case 'm':result+='M';break;
                case '_':result+=' ';break;
                default:result+=sb.charAt(i);break;

            }
        }
        return result;
    }

}