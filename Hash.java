import org.mindrot.jbcrypt.BCrypt;
public class Hash {
    public static void main(String[] args) {
        System.out.println(BCrypt.hashpw("123456", BCrypt.gensalt(10)));
    }
}
