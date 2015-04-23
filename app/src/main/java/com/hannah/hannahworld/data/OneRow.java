package com.hannah.hannahworld.data;
public class OneRow{
    public String word;
    public int id;
    public int week;
    public OneRow(){;}

    public OneRow(int id, int week, String word)
    {
        this.setId(id);
        this.setWeek(week);
        this.setWord(word);

    }

    public int getId() {
        return id;
    }
    public int getWeek(){
        return week;
    }
    public String getWord(){
        return word;
    }
    void setId(int id){
        this.id = id;
    }
    void setWeek(int week){
        this.week= week;
    }
    void setWord(String word){
        this.word = word;
    }
}
