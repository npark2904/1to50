package Game;

public class Person {
	private String name;
	private String score;
	private String date;
	private String comment;
	
	public String getName() {
		return name;
	}
	public String getScore() {
		return score;
	}
	public String getDate() {
		return date;
	}
	public String getComment() {
		return comment;
	}
	public void setName(String name){
		this.name = name;
	}
	public void setScore(String score){
		this.score = score;
	}
	public void setDate(String date){
		this.date = date;
	}
	public void setComment(String comm){
		this.comment = comm;
	}
	
	@Override
	public String toString(){
		return name + "/" + score + "/" + comment + "/" + date;
	}
}