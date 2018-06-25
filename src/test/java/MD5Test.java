import java.security.MessageDigest;

/**
 * Created by msi on 2018/6/9.
 */
public class MD5Test {
    public   final   static  String MD5(String name) {
        char  hexDigits[] = {  '0' ,  '1' ,  '2' ,  '3' ,  '4' ,  '5' ,  '6' ,  '7' ,  '8' ,  '9' ,
                'a' ,  'b' ,  'c' ,  'd' ,  'e' ,  'f'  };
        try  {
            byte [] strTemp = name.getBytes();
            MessageDigest mdTemp = MessageDigest.getInstance("MD5" );
            mdTemp.update(strTemp);
            byte [] md = mdTemp.digest();
            int  j = md.length;
            char  str[] =  new   char [j *  2 ];
            int  k =  0 ;
            for  ( int  i =  0 ; i < j; i++) {
                byte  byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4  &  0xf ];
                str[k++] = hexDigits[byte0 & 0xf ];
            }
            return   new  String(str);
        } catch  (Exception e) {
            return   null ;
        }
    }

    public   static   void  main(String[] args) {
        // MD5_Test aa = new MD5_Test();
//        admin123456
        System.out.print(MD5Test.MD5("admin123456"));
    }
}
