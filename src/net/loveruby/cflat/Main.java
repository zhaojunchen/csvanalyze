package net.loveruby.cflat;
import java.io.*;

public class Main {
    /** 原始csv文件的结构字段分布
     * word,phonetic,definition,translation,pos,
     * collins,oxford,tag,bnc,frq,
     * exchange,detail,audio
     */
    /**
     * 分隔符
     */
    public static char splitsymbol = '#';

    /**
     * 文件根目录
     */
    public static String rootpath = "C:\\Users\\zhao\\Desktop\\dict\\ECDICT\\stardict\\";
    /**
     * 输出文件名
     */
    public static String gk = "gk.csv";//高考
    public static String zk = "zk.csv";//中考
    public static String cet4 = "cet4.csv";//四级
    public static String cet6 = "cet6.csv";//六级
    public static String toefl = "toefl.csv";//托福
    public static String ielts = "ielts.csv";//雅思
    public static String gre = "gre.csv";
    public static String ky = "ky.csv";

    /**
     * tag标识
     */
    public static short tag_zk = 0x0001;//中考 0
    public static short tag_gk = 0x0002;//高考 1
    public static short tag_cet4 = 0x0004;//四级 2
    public static short tag_cet6 = 0x0008;//六级 3
    public static short tag_toefl = 0x0010;//托福 4
    public static short tag_ielts = 0x0020;//雅思 5
    public static short tag_gre = 0x0040;// gre 6
    public static short tag_ky = 0x0080;//ky 7

    /**
     * 对开源词库预处理
     */
    public static void preparecsv(String filename, String filename_des) throws Exception {
        File csv = new File(filename); // CSV文件路径
        FileWriter fw = new FileWriter(filename_des);
        BufferedReader br = new BufferedReader(new FileReader(csv));
        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer temp = new StringBuffer();
        String line = null;
        String tempstring = null;
        String[] tempbuffer;
        String splitstring = Character.toString(splitsymbol);
        char ch;
        int i, isclose;
        long count = 1;

        //* 添加正确的分隔符和id标识*//*
        try {
            while ((line = br.readLine()) != null) {
                line = String.valueOf(count) + "," + line;
                count++;
                isclose = 0;
                temp.setLength(0);
                for (i = 0; i < line.length(); i++) {
                    ch = line.charAt(i);
                    if (ch == '"') {
                        isclose = (isclose + 1) % 2;
                    } else if (ch == ',' && isclose == 0) {
                        ch = splitsymbol;
                        temp.append(ch);
                    } else if (ch == splitsymbol) {
                    } else {
                        temp.append(ch);
                    }
                }
                /**
                 * 舍弃短语
                 * */
                tempstring = temp.toString();
                tempbuffer = tempstring.split(splitstring, -1);
                if (tempbuffer.length != 14) {
                    System.out.println("error");
                    continue;
                }
                tempstring = tempbuffer[1].trim();
                if (tempstring.contains(" ")) {
                    continue;
                }
                temp.append("\n");
                stringBuffer.append(temp);
            }
            fw.write(stringBuffer.toString());
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取分表
     * A输入预处理的词库文件
     * B输入处理后的保存文件
     * select 选择类型gk..
     */
    public static void word(String A, String B, String select, short _tag) throws Exception {
        File file = new File(A);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line = null;
        StringBuffer stringBuffer = new StringBuffer();
        int position = 8; //单词类型在csv的位置
        int count = 0;
        String tag = String.valueOf(_tag);
        String[] buffer;
        String splitstring = Character.toString(splitsymbol);
        String buffer0, buffer1, buffer2, buffer3, buffer4, buffer8, buffer11;

        /**
         * wordid,word,phonetic,definition,translation,pos,
         * collins,oxford,tag,bnc,frq,
         * exchange,detail,audio
         * 0 1 2 3 4 11 8 (tag)
         */
        StringBuffer onlyword = new StringBuffer();
        try {
            onlyword.setLength(0);
            while ((line = br.readLine()) != null) {
                buffer = line.split(splitstring, -1);
                buffer8 = buffer[position].trim();
                if (!buffer8.contains(select)) {
                    continue;
                }
                count++;
                onlyword.append(buffer[1].trim() + "\n");
                System.out.println(buffer[1].trim());
                buffer0 = buffer[0].trim() + splitstring;
                buffer1 = buffer[1].trim() + splitstring;
                buffer2 = buffer[2].trim() + splitstring;
                buffer3 = buffer[3].trim() + splitstring;
                buffer4 = buffer[4].trim() + splitstring;
                buffer11 = buffer[11].trim() + splitstring;
                stringBuffer.append(buffer0);
                stringBuffer.append(buffer1);
                stringBuffer.append(buffer2);
                stringBuffer.append(buffer3);
                stringBuffer.append(buffer4);
                stringBuffer.append(buffer11);
                stringBuffer.append(tag + "\n");

            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        FileWriter fw = new FileWriter(B);
        FileWriter fw_onlyword = new FileWriter(rootpath + select + "_onlyword.csv");
        fw.write(stringBuffer.toString());
        fw_onlyword.write(onlyword.toString());
        fw.close();
        fw_onlyword.close();
        System.out.println(B + " items is " + count);
    }


    /**
     * 获取分表的url下载链接
     * 使用 wget -i url.txt -P ./
     */

    public static void urltxt(String file) throws Exception {
        File csv = new File(file); // CSV文件路径
        BufferedReader br = new BufferedReader(new FileReader(csv));
        String line = null;
        StringBuffer stringBuffer = new StringBuffer();
        while ((line = br.readLine()) != null) {
            stringBuffer.append("https://ssl.gstatic.com/dictionary/static/sounds/oxford/");
            stringBuffer.append(line.trim());
            stringBuffer.append("--_gb_1.mp3\n");
        }
        FileWriter fw = new FileWriter(file + "_url.txt");
        fw.write(stringBuffer.toString());
        fw.close();


    }

    /**
     * 分表汇总
     * A输入预处理的词库文件
     * B输入处理后的保存文件
     * select选择类型
     */
    public static void wordall(String A, String B) throws Exception {
        File file = new File(A); // CSV文件路径
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line = null;
        StringBuffer stringBuffer = new StringBuffer();
        int position = 8; //单词类型在csv的位置
        int count = 0;
        String[] buffer;
        String splitstring = Character.toString(splitsymbol);
        String buffer0, buffer1, buffer2, buffer3, buffer4, buffer8, buffer11;

        /**
         * wordid,word,phonetic,definition,translation,pos,
         * collins,oxford,tag,bnc,frq,
         * exchange,detail,audio
         * 0 1 2 3 4 11 8 (tag)
         */
        short tag = 0;
        StringBuffer onlyword = new StringBuffer();
        try {
            onlyword.setLength(0);
            while ((line = br.readLine()) != null) {
                buffer = line.split(splitstring, -1);
                buffer8 = buffer[position].trim();
                tag = 0;
                if (buffer8.contains("zk")) {
                    tag |= tag_zk;
                }
                if (buffer8.contains("gk")) {
                    tag |= tag_gk;
                }
                if (buffer8.contains("cet4")) {
                    tag |= tag_cet4;
                }
                if (buffer8.contains("cet6")) {
                    tag |= tag_cet6;
                }
                if (buffer8.contains("toefl")) {
                    tag |= tag_toefl;
                }
                if (buffer8.contains("ielts")) {
                    tag |= tag_ielts;
                }
                if (buffer8.contains("gre")) {
                    tag |= tag_gre;
                }
                if (buffer8.contains("ky")) {
                    tag |= tag_ky;
                }
                if (tag == 0) {
                    continue;
                }
                count++;
                //buffer0 = buffer[0].trim() + splitstring;
                buffer0 = String.valueOf(count) + splitstring;
                buffer1 = buffer[1].trim() + splitstring;
                buffer2 = buffer[2].trim() + splitstring;
                buffer3 = buffer[3].trim() + splitstring;
                buffer4 = buffer[4].trim() + splitstring;
                buffer11 = buffer[11].trim() + splitstring;
                stringBuffer.append(buffer0);
                stringBuffer.append(buffer1);
                stringBuffer.append(buffer2);
                stringBuffer.append(buffer3);
                stringBuffer.append(buffer4);
                stringBuffer.append(buffer11);
                stringBuffer.append(String.valueOf(tag) + "\n");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        FileWriter fw = new FileWriter(B);
        fw.write(stringBuffer.toString());
        fw.close();
        System.out.println(B + " items is " + count);
    }

    /**
     * 表的检查
     */
    public static void check(String A) throws FileNotFoundException {
        File csv = new File(A);
        BufferedReader br = new BufferedReader(new FileReader(csv));
        String line = null;
        int position = 6; //单词类型在csv的位置
        String[] buffer;
        String splitstring = Character.toString(splitsymbol);
        String tag_string;

        /**
         *     public static char tag_zk = 0x0001;//中考 0
         *     public static char tag_gk = 0x0002;//高考 1
         *     public static char tag_cet4 = 0x0004;//四级 2
         *     public static char tag_cet6 = 0x0008;//六级 3
         *     public static char tag_toefl = 0x0010;//托福 4
         *     public static char tag_ielts = 0x0020;//雅思 5
         *     public static char tag_gre = 0x0040;// gre
         *     public static char tag_ky = 0x0080;//ky*/
        short tag;
        try {
            while ((line = br.readLine()) != null) {
                buffer = line.split(splitstring, -1);
                tag_string = buffer[position];
                tag = Short.valueOf(buffer[6].trim());
                System.out.print(buffer[1] + " : ");
                if ((tag & tag_zk) == tag_zk) {
                    System.out.print("zk ");
                }
                if ((tag & tag_gk) == tag_gk) {
                    System.out.print("gk ");
                }
                if ((tag & tag_cet4) == tag_cet4) {
                    System.out.print("cet4 ");
                }
                if ((tag & tag_cet6) == tag_cet6) {
                    System.out.print("cet6 ");
                }
                if ((tag & tag_ielts) == tag_ielts) {
                    System.out.print("ielts ");
                }
                if ((tag & tag_toefl) == tag_toefl) {
                    System.out.print("toefl ");
                }
                if ((tag & tag_gre) == tag_gre) {
                    System.out.print("gre ");
                }
                if ((tag & tag_ky) == tag_ky) {
                    System.out.print("ky");
                }
                System.out.println("");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }


    public static void main(String[] args) {
        // write your code here
        String A = "stardict.csv";
        String B = "preparedict.csv";
        String C = "target.csv";
        String a = "n. 研磨料\na. 有研磨作用的";
        System.out.println(a);

        /*try {  // 文件预处理
            preparecsv(rootpath + A, rootpath + B);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }*/

        /*try {
            word(rootpath + B, rootpath + ky, "ky", tag_ky);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }*/

        /**
         *生成分表的汇总
         * */
        /*try {
            wordall(rootpath + B, rootpath + "target_1.csv");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }*/
        /**
         * 测试汇总表
         * */
        /*try {
            check("C:\\Users\\zhao\\Desktop\\dict\\ECDICT\\stardict\\target.csv");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }*/

       /* try {
            urltxt(rootpath + "toefl_onlyword.csv");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }*/
    }
}
