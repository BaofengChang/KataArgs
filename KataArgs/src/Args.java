import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

/**
 * @program: KataArgs
 * @description: give a string of arguments, then suit the flag and value
 * @author: Chang Baofeng
 * @create: 2019-06-04 14:25
 **/

public class Args {
    public static void main(String[] args) {
        String string = "-l -p 8080 -d /usr/logs";
        HashMap<String,Object> argumentsHashMap = new HashMap<>();
        argumentsHashMap = suitFlagWithValues(string);
    }
    public static HashMap<String,Object> suitFlagWithValues(String arguments){
        HashMap<String,Object> argumentsHashMap = new HashMap<>();
        Stack<String> argsStack = changeStringToStack(arguments);
        while (!argsStack.empty()){
            String flag = argsStack.pop().substring(1);//get flag
            String tempValue = argsStack.peek();
            if(tempValue.length()==2&&tempValue.charAt(0)=='-'&&(tempValue.charAt(1)>='a'&&tempValue.charAt(1)<='z')){
                // the value of the current flag is null,set the default value;
                argumentsHashMap.put(flag,"default value");
                continue;
            }else {
                // the value of the current flag is not null
                if(tempValue.contains(",")){ // is a list
                    String [] strings = argsStack.pop().split(",");
                    if(isStringsNumeric(tempValue)){ //the list is form of number
                        if(tempValue.contains(".")){
                            //double
                            List<Double> listValue = new ArrayList<>();
                            for(String s:strings){
                                listValue.add(Double.parseDouble(s));
                            }
                            argumentsHashMap.put(flag,listValue);
                        }else {
                            //Integer
                            List<Integer> listValue = new ArrayList<>();
                            for(String s:strings){
                                listValue.add(Integer.valueOf(s));
                            }
                            argumentsHashMap.put(flag,listValue);
                        }
                    }else { //the list is form of string
                        List<String> listValue = new ArrayList<>();
                        for(String s:strings){
                            listValue.add(s);
                        }
                        argumentsHashMap.put(flag,listValue);
                    }
                }else { // is not a list
                    if(isNumeric(tempValue)){
                        //is single number
                        if(tempValue.contains(".")){
                            //double
                            Double value = Double.parseDouble(argsStack.pop());
                            argumentsHashMap.put(flag,value);
                        }else {
                            //int
                            Integer value = Integer.parseInt(argsStack.pop());
                            argumentsHashMap.put(flag,value);
                        }
                    }else {
                        argumentsHashMap.put(flag,argsStack.pop());
                    }
                }
            }
        }
        return argumentsHashMap;
    }

    /**
     * change the string to stack, the top is the head of string
     * @param string
     * @return stack<String>
     */
    public static Stack<String> changeStringToStack(String string){
        Stack<String> stack = new Stack<>();
        String [] strs = string.split(" ");
        for(int i=strs.length-1;i>=0;i--){
            stack.push(strs[i]);
        }
        return stack;
    }

    /**
     * judge the strings is numbers or strings
     * @param strings
     * @return true is numbers,false is strings
     */
    public static boolean isStringsNumeric(String strings){
        String [] strs = strings.split(",");
        for(String s:strs){
            try {
                new BigDecimal(s);
            }catch (Exception e){
                return false;
            }
        }
        return true;
    }

    /**
     * judge a single string is a number or not
     * @param string
     * @return true if the string is a number and false is not
     */
    public static boolean isNumeric(String string){
        try {
            new BigDecimal(string);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
