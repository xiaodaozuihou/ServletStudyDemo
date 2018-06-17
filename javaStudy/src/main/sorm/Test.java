import bean.User;
import core.Query;
import core.QueryFactory;

import java.util.List;

public class Test {
    public static void main(String[] args) {
        Query query = QueryFactory.createQuery();
        String sql = "select * from user";
        List<User> list = query.queryRows(sql, User.class, null);
        if (list != null && !list.isEmpty()){
            for (User user:list) {
                System.out.println(user.getName());
            }
        }

    }
}
