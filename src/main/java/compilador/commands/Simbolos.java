package compilador.commands;

public class Simbolos {
    private String name;
    private int type;
    private String value;

    public static final int REAL =0;
    public static final int INT =1;

    public String generateCode(){
        String str;
        if(type==INT) str= "int "+name+";";
        else str= "double "+name+";";
        return str;
    }

    public Simbolos(String name, int type, String value){
        this.name=name;
        this.type=type;
        this.value=value;
    }

    public String getName(){
        return name;
    }

    public int getType(){
        return type;
    }

    public String getValue(){
        return value;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setType(int type){
        this.type = type;
    }

    public void setValue(String value){
        this.value = value;
    }

    @Override
    public String toString(){
        return "GyhSymbol [name=" + this.name + "type=" + type + "value=" + value + "]";
    }
}
