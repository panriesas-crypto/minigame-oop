import java.util.*;
public class GameManager {
    private final Player player=new Player(35,450);
    private final List<Floor> floors=new ArrayList<>();
    private final List<Ladder> ladders=new ArrayList<>();
    public GameManager() {
        for(int n=1;n<=4;n++) floors.add(new Floor(n,490-(n-1)*120));
        for(int n=1;n<4;n++) ladders.add(new Ladder(n%2==1?480:90,490-n*120,120));
    }
    public Player getPlayer(){return player;}
    public List<Floor> getFloors(){return Collections.unmodifiableList(floors);}
    public List<Ladder> getLadders(){return Collections.unmodifiableList(ladders);}
}
