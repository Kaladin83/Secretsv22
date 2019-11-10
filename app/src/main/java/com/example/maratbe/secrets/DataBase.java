package com.example.maratbe.secrets;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;

public class DataBase implements Constants  {
    //public String ip = "sql11.freesqldatabase.com:3306";
    public String ip = "sql7.freesqldatabase.com:3306";
   // public String ip = "10.239.64.62:3306";
    //public String ip = "10.239.189.130:3306";

    public String classs = "com.mysql.jdbc.Driver";
    //public String classs = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    public String dbName = "sql7311482";
    public String userName = "sql7311482";
    public String password = "MUX5Ve77gN";
   /* public String dbName = "mysql";
    public String userName = "marat";
    public String password = "marat";*/
  //  public String userName = "root";
  //  public String password = "marat6753630";
    private StringBuffer query;
    private int lastTouched, userIndex;
    private PreparedStatement stmt = null;
    private Connection conn = null;
    private String itemIds= "";
    private ArrayList<Item> tempItemList ;

    ///
    //mbeinerdev83@gmail.com
    ///

         //TABLE DECLARATIONS
    /*CREATE TABLE sql7311482.users ( user_id INT(10) NOT NULL AUTO_INCREMENT, user_name VARCHAR(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL , id_type CHAR(1) NOT NULL , email varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
    registration_date DATE NOT NULL ) ENGINE = InnoDB CHARSET=utf8 COLLATE utf8_general_ci;
    ALTER TABLE users ADD PRIMARY KEY( user_id, user_name, id_type, email);
    ALTER TABLE users ADD INDEX( user_id);

    CREATE TABLE sql7311482.items ( item_id int(10), user_name CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL , text TEXT CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,  DATE NOT NULL ,
    score int(2), comments TEXT CHARACTER SET utf8 COLLATE utf8_general_ci, votes int(6)) ENGINE = InnoDB CHARSET=utf8 COLLATE utf8_general_ci;
    ALTER TABLE items ADD PRIMARY KEY( item_id);
    ALTER TABLE items ADD INDEX( user_name);

    CREATE TABLE sql7311482.tags ( tag_name VARCHAR(20)) ENGINE = InnoDB CHARSET=utf8 COLLATE utf8_general_ci;

    CREATE TABLE sql7311482.votes ( item_id INT(10) NOT NULL , type CHAR(1) NOT NULL , user_id int(10) NOT NULL ) ENGINE = InnoDB CHARSET=utf8 COLLATE utf8_general_ci;
    ALTER TABLE votes ADD PRIMARY KEY( item_id, user_id);

    CREATE TABLE sql7311482.comments  (comment_id  INT(10) NOT NULL ,  text  TEXT CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,  stars  INT(1) NOT NULL ,  item_id  INT(10) NOT NULL ,  parent_id  INT(10) NOT NULL ,
    date_added  DATE NOT NULL ,  user_name  VARCHAR(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL, likes int(6), dislikes int(6) ) ENGINE = InnoDB CHARSET=utf8 COLLATE utf8_general_ci;
    ALTER TABLE comments ADD PRIMARY KEY(comment_id);
    ALTER TABLE comments ADD INDEX(item_id);

    CREATE TABLE sql7311482.tags_and_items ( tag_name VARCHAR(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL , item_id INT(10) NOT NULL ) ENGINE = InnoDB CHARSET=utf8 COLLATE utf8_general_ci;
    ALTER TABLE tags_and_items ADD PRIMARY KEY(comment_id);
    ALTER TABLE tags_and_items ADD PRIMARY KEY( tag_name, item_id);*/


    @SuppressLint("NewApi")

    public DataBase()
    {}

    public void connect() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String ConnURL = null;
        try {

            Class.forName(classs);
            ConnURL = "jdbc:mysql://" + ip + "/"
                    +dbName+ "?useUnicode=true&characterEncoding=UTF-8" ;
           // ConnURL = "jdbc:sqlserver://localhost:3306/;databaseName=secrets;user=secrets;password=Marat!";
           // conn = DriverManager.getConnection(ConnURL);
             conn = DriverManager.getConnection(ConnURL, userName, password);
          /*  conn =DriverManager.getConnection(
                    "jdbc:mysql://localhost:8080/secrets","root","marat6753630");*/
        } catch ( SQLException se) {
            Log.e("ERROR", se.getErrorCode()+": "+se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("ERROR", e.getMessage());
        }
    }

    public int insertUser(User user)
    {
        int returnCode = 0;
        query = new StringBuffer();
        connect();

        createSelectUserRegisterCheckQuery(user.getUserName().toUpperCase());
        returnCode = fetchData("fetchCheckUserData", false);
        if (returnCode ==  0)
        {
            query = new StringBuffer();
            createInsertUserQuery(user);
            returnCode = insertData(true);
        }

        return returnCode;
    }

    public int selectLoginUser(String email, char idType)
    {
        int returnCode = 0;
        query = new StringBuffer();
        connect();

        createSelectUserLoginCheckQuery(email.toUpperCase(), idType);
        returnCode = fetchData("fetchCheckUserData", false);

        return returnCode;
    }

    public int insertNewTag(String tagName) {
        query = new StringBuffer();
        connect();

        createInsertNewTagQuery(tagName);
        return insertData(true);
    }

    public int insertItem(String userName, String text, int score, String[] tags, boolean connect, boolean isClose)
    {
        query = new StringBuffer();
        int rc = -1;
        text = text.replaceAll("'", "~");

        if (connect)
        {
            connect();
        }

        createSelectNextAvailableItemIdQuery();
        int itemId = fetchData("fetchAvailableItemId", false);
        if (itemId > 0)
        {
            for (int i = 0; i < tags.length; i++)
            {
                rc = insertTags(tags[i], itemId);
            }

            if (rc == 0)
            {
                query = new StringBuffer();
                createInsertItemQuery(userName ,text, score, itemId);
                rc = insertData(isClose);
            }
        }

        return rc;
    }

    public int insertTags(String tagName, int itemId)
    {
        query = new StringBuffer();

        createInsertTagsQuery(tagName, itemId);
        return insertData(false);
    }

    public int insertVote(int itemId, char type, String userName, int vote, boolean isConnect, boolean isClose)
    {
        query = new StringBuffer();
        int rc = 0;

        if (isConnect)
        {
            connect();
        }
        createUpdateVotesQuery(itemId, vote);
        if (insertData(false) == 0)
        {
            query = new StringBuffer();
            createInsertVoteQuery(itemId, type, userName);
            rc = insertData(isClose);
        }
        return rc;
    }

    public int insertLikeComment(int commentId, int likes, boolean isConnect, boolean isClose)
    {
        query = new StringBuffer();

        if (isConnect)
        {
            connect();
        }

        createLikeCommentsQuery(commentId, likes);
        return insertData(isClose);
    }

    public int insertDislikeComment(int commentId, int dislikes, boolean isConnect, boolean isClose)
    {
        query = new StringBuffer();

        if (isConnect)
        {
            connect();
        }

        createDislikeCommentsQuery(commentId, dislikes);
        return insertData(isClose);
    }

    public int insertComment(int itemId, Item.Comment comment, int numOfComments, int parentId)
    {
        int returnCode = 0;
        query = new StringBuffer();
        comment.setText(Util.encodeStringUrl(comment.getText()).replaceAll("'", "~"));
        comment.setDateAdded(comment.getDateAdded().replaceAll("/", "-"));

        connect();
        createInsertCommentQuery(itemId, comment, parentId);
        if (insertData(false) == 0)
        {
            query = new StringBuffer();
            createUpdateCommentsQuery(itemId, numOfComments);
            returnCode = insertData(false);
        }

        return returnCode;
    }

    public int updateScore(int itemId, int score, boolean isConnect, boolean isClose)
    {
        query = new StringBuffer();
        if (isConnect)
        {
            connect();
        }
        createUpdateScoreQuery(itemId, score);

        return insertData(isClose);
    }

    public int updateComments(int itemId, int commentId, String ts, String text, int stars, boolean isConnect, boolean isClose)
    {
        text = Util.encodeStringUrl(text).replaceAll("'","~");
        query = new StringBuffer();
        if (isConnect)
        {
            connect();
        }
        createUpdateCommentsQuery(itemId, commentId, stars, text, ts);

        return insertData(isClose);
    }

    public int updateUserScore(char type, String userName, int score, boolean isConnect, boolean isClose)
    {
        query = new StringBuffer();
        if (isConnect)
        {
            connect();
        }

        if (type == 'S')
        {
            createUpdateSecretsScoreQuery(score, userName);
        }
        else
        {
            createUpdateVotesScoreQuery(score, userName);
        }


        return insertData(isClose);
    }

    public int deleteVote(int itemId, String userName, int vote, boolean isConnect, boolean isClose)
    {
        query = new StringBuffer();
        int rc = 0;

        if (isConnect)
        {
            connect();
        }
        createUpdateVotesQuery(itemId, vote);
        if (insertData(false) == 0)
        {
            query = new StringBuffer();
            createDeleteVoteQuery(itemId, userName);
            rc = insertData(isClose);
        }
        return rc;
    }

    public int deleteComment(int itemId, int commentId, int numOfComments, boolean isConnect, boolean isClose)
    {
        int rc = -1;
        query = new StringBuffer();
        if (isConnect)
        {
            connect();
        }
        query = new StringBuffer();
        createDeleteCommentQuery(itemId, commentId);
        if (insertData(false) == 0)
        {
            query = new StringBuffer();
            createUpdateCommentsQuery(itemId, numOfComments);
            rc = insertData(isClose);
        }

        return rc;
    }

    public int selectTopTenData(boolean isClose)
    {
        itemIds= "";
        int returnCode;
        query = new StringBuffer();

        connect();
        tempItemList = new ArrayList<>();
        createLimitedTopTenQuery();
        returnCode = fetchData("fetchItemsIds", false);
        if(returnCode == 0) {
            query = new StringBuffer();
            createSelectTopTenQuery();
            returnCode = fetchData("fetchItemsData", isClose);
            MainActivity.setTopTenList(tempItemList);
        }

        return returnCode;
    }

    public int selectAllSecretsData(int lastTouched, String order, boolean isConnect, boolean isClose)
    {
        itemIds= "";
        int returnCode;
        query = new StringBuffer();
        this.lastTouched = lastTouched;
        order = order.equals("rating")? "score": order;

        if (isConnect)
        {
            connect();
        }
        if (lastTouched == 0)
        {
            tempItemList = new ArrayList<>();
        }

        createLimitedItemsQuery(order, lastTouched);
        returnCode = fetchData("fetchItemsIds", false);
        if(returnCode == 0)
        {
            query = new StringBuffer();
            createSelectAllQuery(order);
            returnCode =  fetchData("fetchItemsData", isClose);
            MainActivity.setSecretsList(tempItemList);
        }

        return returnCode;
    }

    public int selectTags(boolean isConnect, boolean isClose)
    {
        query = new StringBuffer();

        if (isConnect)
        {
            connect();
        }
        createSelectAllTagsQuery();
        return fetchData("fetchAllTagsData", isClose);
    }

    public int selectUserData(User user, int index)
    {
        itemIds= "";
        int returnCode;
        query = new StringBuffer();
        userIndex = index;

        connect();
        createItemIdsByUserQuery(user.getUserName());
        returnCode =  fetchData("fetchUsersItemIds", false);

        if(returnCode == 0)
        {
            query = new StringBuffer();
            createUsersCommentsQuery(user.getUserName());
            returnCode =  fetchData("fetchUsersComments", false);
        }

        if(returnCode == 0)
        {
            query = new StringBuffer();
            createUsersVotesQuery(user.getUserName());
            returnCode =  fetchData("fetchUsersVotes", false);
        }

        MainActivity.getLocalStorage().storeUserBasic(user);
        return returnCode;
    }

    private void createItemIdsByUserQuery(String userName)
    {
        query.append("SELECT item_id FROM items where user_name = '");
        query.append(userName);
        query.append("';");
    }

    private void createUsersCommentsQuery(String userName)
    {
        query.append("SELECT item_id, count(*) as num_of_comments FROM comments where user_name = '");
        query.append(userName);
        query.append("' group by item_id;");
    }

    private void createUsersVotesQuery(String userName)
    {
        query.append("SELECT item_id FROM votes v join users u on (v.user_id = u.user_id) where u.user_name = '");
        query.append(userName);
        query.append("';");
    }

    private void createItemsVotesQuery(int itemId)
    {
        query.append("SELECT user_name, type FROM votes v join users u on (v.user_id = u.user_id) where item_id = ");
        query.append(itemId);
        query.append(";");
    }

    public int selectUsersSecrets(String secrets, int userIndex, int offset, boolean isConnect, boolean isClose) {
        if (secrets.length() > 0)
        {
            query = new StringBuffer();
            if (isConnect)
            {
                connect();
            }
            createLimitedItemsByUserQuery(secrets);
            tempItemList = offset == 0? new ArrayList<Item>(): MainActivity.getUser(userIndex).getUsersSecrets();
            int returnCode =  fetchData("fetchItemsData", isClose);
            MainActivity.getUser(userIndex).setUsersItemsBySecretsList(tempItemList);
            return returnCode;
        }
        return 0;
    }

    public int selectUsersPinned(String pinned, int userIndex, int offset, boolean isConnect, boolean isClose) {
        if (pinned.length() > 0)
        {
            query = new StringBuffer();
            if (isConnect)
            {
                connect();
            }
            createLimitedItemsByUserQuery(pinned);
            tempItemList = offset == 0? new ArrayList<>(): MainActivity.getUser(userIndex).getUsersPinned();
            int returnCode =  fetchData("fetchItemsData", isClose);
            MainActivity.getUser(userIndex).setUsersPinned(tempItemList);
            return returnCode;
        }
        return 0;
    }

    public int selectUsersComments(String comments, int userIndex, int offset, boolean isConnect, boolean isClose) {
        if (comments.length() > 0)
        {
            query = new StringBuffer();
            if (isConnect)
            {
                connect();
            }
            createLimitedItemsByUserQuery(comments);
            tempItemList = offset == 0? new ArrayList<>(): MainActivity.getUser(userIndex).getUsersComments();
            int returnCode =  fetchData("fetchItemsData", isClose);
            MainActivity.getUser(userIndex).setUsersItemsByCommentsList(tempItemList);
            return returnCode;
        }
        return 0;
    }

    public int selectUsersVotes(String votes, int userIndex, int offset, boolean isConnect, boolean isClose) {
        if (votes.length() > 0)
        {
            query = new StringBuffer();
            if (isConnect)
            {
                connect();
            }
            createLimitedItemsByUserQuery(votes);
            tempItemList = offset == 0? new ArrayList<>(): MainActivity.getUser(userIndex).getUsersVotes();
            int returnCode =  fetchData("fetchItemsData", isClose);
            MainActivity.getUser(userIndex).setUsersItemsByVotesList(tempItemList);
            return returnCode;
        }
        return 0;
    }

    public int selectItemsByTagData(String tagName, String order, int offset)
    {
        itemIds= "";
        int returnCode;
        query = new StringBuffer();
        this.lastTouched = offset;
        order = order.equals("rating")? "score": order;

        if (offset == 0)
        {
            tempItemList = new ArrayList<>();
        }

        connect();
        createLimitedItemsByTagsQuery(tagName, order, offset);
        returnCode = fetchData("fetchItemsIds", false);
        if(returnCode == 0)
        {
            query = new StringBuffer();
            createSelectAllQuery(order);
            returnCode =  fetchData("fetchItemsData", true);
            MainActivity.setItemByTagList(tempItemList);
        }

        return returnCode;
    }

    public int selectCommentsData(ArrayList<Item> list, int itemIndex, int offset)
    {
        this.lastTouched = offset;
        int returnCode;
        query = new StringBuffer();

        connect();
        createSelectCommentsQuery(list, itemIndex, offset);
        returnCode = fetchData("fetchCommentsData", list, itemIndex, true);

        return returnCode;
    }

    private void createInsertUserQuery(User user)
    {
        query.append("INSERT INTO users (user_name, id_type, email, registration_date) VALUES('");
        query.append(user.getUserName());
        query.append("', '");
        query.append(user.getIdType());
        query.append("', '");
        query.append(user.getEmail());
        query.append("' , CURDATE()+0);");
    }

    private void createInsertNewTagQuery(String tagName) {
        query.append("INSERT into tags (tag_name) VALUES('");
        query.append(tagName);
        query.append("';");
    }

    private void createInsertItemQuery(String userName, String text, int score, int itemId)
    {
        query.append("INSERT into items (item_id, user_name, text, date, score) VALUES(");
        query.append(itemId);
        query.append(", '");
        query.append(userName);
        query.append("', '");
        query.append(text);
        query.append("', NOW(), ");
        query.append(score);
        query.append(");");
    }

    private void createInsertTagsQuery(String tagName, int itemId)
    {
        query.append("INSERT INTO tags_and_items (tag_name, item_id) VALUES('");
        query.append(tagName);
        query.append("', ");
        query.append(itemId);
        query.append(");");
    }

    private void createInsertCommentQuery(int itemId, Item.Comment comment, int parentId)
    {
        query.append("INSERT INTO comments (comment_id, text, stars, item_id, parent_id, date_added, user_name) ");
        query.append("SELECT t1.comment_id + 1 AS comment_id, '");
        query.append(comment.getText());
        query.append("', ");
        query.append(comment.getStars());
        query.append(", ");
        query.append(itemId);
        query.append(", ");
        query.append(parentId);
        query.append(", '");
        query.append(comment.getDateAdded());
        query.append("' , '");
        query.append(comment.getUserName());
        query.append("' FROM comments AS t1 ");
        query.append("LEFT JOIN comments AS t2 ON t1.comment_id + 1 = t2.comment_id ");
        query.append("WHERE t2.comment_id IS NULL ");
        query.append("ORDER BY t1.comment_id LIMIT 1; ");
    }

    private void createInsertVoteQuery(int itemId, char type, String userName)
    {
        query.append("INSERT INTO votes (item_id, type, user_id) SELECT ");
        query.append(itemId);
        query.append(", '");
        query.append(type);
        query.append("', user_id FROM users WHERE user_name = '");
        query.append(userName);
        query.append("';");
    }

    private void createUpdateVotesQuery(int itemId, int votes)
    {
        query.append("UPDATE items SET votes = ");
        query.append(votes);
        query.append(" WHERE item_id = ");
        query.append(itemId);
        query.append(";");
    }

    private void createUpdateCommentsQuery(int itemId, int comments)
    {
        query.append("UPDATE items SET comments = ");
        query.append(comments);
        query.append(" WHERE item_id = ");
        query.append(itemId);
        query.append(";");
    }

    private void createLikeCommentsQuery(int commentId, int likes)
    {
        query.append("UPDATE comments SET likes = ");
        query.append(likes);
        query.append(" WHERE comment_id = ");
        query.append(commentId);
        query.append(";");
    }

    private void createDislikeCommentsQuery(int commentId, int dislikes)
    {
        query.append("UPDATE comments SET dislikes = ");
        query.append(dislikes);
        query.append(" WHERE comment_id = ");
        query.append(commentId);
        query.append(";");
    }

    private void createUpdateSecretsScoreQuery(int score, String userName)
    {
        query.append("UPDATE users ");
        query.append("SET secrets_score = ");
        query.append(score);
        query.append(" WHERE user_name = ");
        query.append(userName);
        query.append(";");
    }

    private void createUpdateVotesScoreQuery(int score, String userName)
    {
        query.append("UPDATE users ");
        query.append("SET votes_score = ");
        query.append(score);
        query.append(" WHERE user_name = ");
        query.append(userName);
        query.append(";");
    }

    private void createUpdateCommentsQuery(int itemId, int commentId, int stars, String text, String TS)
    {
        query.append("UPDATE comments ");
        query.append("SET text = '");
        query.append(text);
        query.append("', stars = ");
        query.append(stars);
        query.append(", date_added = '");
        query.append(TS);
        query.append("' WHERE item_id = ");
        query.append(itemId);
        query.append(" and comment_id = ");
        query.append(commentId);
        query.append(";");
    }

    private void createSelectUserRegisterCheckQuery(String user)
    {
        query.append("SELECT user_name, email, id_type FROM users ");
        query.append("WHERE UPPER(user_name) = '");
        query.append(user);
        query.append("';");
    }

    private void createSelectUserLoginCheckQuery(String email, char idType)
    {
        query.append("SELECT user_name, email, type FROM users ");
        query.append("WHERE UPPER(email) = '");
        query.append(email);
        query.append("' AND type = '");
        query.append(idType);
        query.append("';");
    }

    private void createSelectNextAvailableItemIdQuery()
    {
        query.append("SELECT t1.item_id+1 AS item_id FROM items AS t1 ");
        query.append("LEFT JOIN items AS t2 ON t1.item_id+1 = t2.item_id ");
        query.append("WHERE t2.item_id IS NULL ORDER BY t1.item_id LIMIT 1;");
    }

    private void createLimitedItemsQuery(String order, int offset)
    {
        query.append("SELECT item_id FROM items order by ");
        query.append(order);
        query.append(" desc limit 10 offset ");
        query.append(offset);
    }

    private void createLimitedTopTenQuery()
    {
        query.append("SELECT item_id FROM items order by score desc limit 10;");
    }


    private void createLimitedItemsByTagsQuery(String tagName, String order, int offset)
    {
        query.append("SELECT t.item_id FROM tags_and_items t JOIN items i ON (t.item_id = i.item_id) where tag_name = '");
        query.append(tagName);
        query.append("' order by ");
        query.append(order);
        query.append(" desc limit 20 offset ");
        query.append(offset);
    }

    private void createSelectTopTenQuery()
    {
        query.append("SELECT items.*, IFNULL(tag_name,' ') as tag_name, IFNULL(comments,0) comments, ");
        query.append("IFNULL(votes_u.user_name,'') as voter, IFNULL(votes_u.type,'') as vote_type ");
        query.append("FROM items left JOIN tags_and_items tags_i ON ");
        query.append("(items.item_id = tags_i.item_id) left JOIN (SELECT item_id, user_name, v.type FROM votes v join users u on (v.user_id = u.user_id) where item_id in (");
        query.append(itemIds);
        query.append(")) votes_u on (items.item_id = votes_u.item_id) WHERE items.item_id in (");
        query.append(itemIds);
        query.append(") order by score desc");
    }

    private void createSelectAllQuery(String order)
    {
        query.append("SELECT items.*, IFNULL(tag_name,' ') as tag_name, IFNULL(comments,0) comments, ");
        query.append("IFNULL(votes_u.user_name,'') as voter, IFNULL(votes_u.type,'') as vote_type ");
        query.append("FROM items left JOIN tags_and_items tags_i ON ");
        query.append("(items.item_id = tags_i.item_id) left JOIN (SELECT item_id, user_name, v.type FROM votes v join users u on (v.user_id = u.user_id) where item_id in (");
        query.append(itemIds);
        query.append(")) votes_u on (items.item_id = votes_u.item_id) WHERE items.item_id in (");
        query.append(itemIds);
        query.append(") order by ");
        query.append(order);
        query.append(" desc;");
    }

    private void createLimitedItemsByUserQuery(String items)
    {
        query.append("SELECT items.*, IFNULL(tag_name,' ') as tag_name, IFNULL(comments,0) comments, ");
        query.append("IFNULL(votes_u.user_name,'') as voter, IFNULL(votes_u.type,'') as vote_type ");
        query.append("FROM items left JOIN tags_and_items tags_i ON ");
        query.append("(items.item_id = tags_i.item_id) left JOIN (SELECT item_id, user_name, v.type FROM votes v join users u on (v.user_id = u.user_id) where item_id in (");
        query.append(items);
        query.append(")) votes_u on (items.item_id = votes_u.item_id) WHERE items.item_id in (");
        query.append(items);
        query.append(") order by date desc");
    }

    private void createSelectTagsQuery(ArrayList<Item> item ,int itemIndex)
    {
        query.append("SELECT tag_name FROM tags_and_items ");
        query.append("WHERE item_id = ");
        query.append(item.get(itemIndex).getItemId());
        query.append(";");
    }

    private void createSelectCommentsQuery(ArrayList<Item> list, int itemIndex, int offset)
    {
        query.append("SELECT text, stars, date_added, user_name, comment_id, likes, dislikes FROM comments ");
        query.append("WHERE item_id = ");
        query.append(list.get(itemIndex).getItemId());
        query.append(" ORDER BY date_added desc limit ");
        query.append(MAX_ITEMS_SHOWS);
        query.append(" offset ");
        query.append(offset);
    }


    private void createSelectAllTagsQuery()
    {
        query.append("SELECT DISTINCT tni.tag_name FROM tags_and_items tni JOIN items ON (tni.item_id = items.item_id) order by tag_name");
    }

    private void createUpdateScoreQuery(int itemId, int score)
    {
        query.append("UPDATE items ");
        query.append("SET score = ");
        query.append(score);
        query.append(" WHERE item_id = ");
        query.append(itemId);
        query.append(";");
    }

    private void createDeleteCommentQuery(int itemId, int commentId) {
        query.append("DELETE FROM comments ");
        query.append(" WHERE item_id = ");
        query.append(itemId);
        query.append(" and comment_id = ");
        query.append(commentId);
        query.append(";");
    }

    private void createDeleteVoteQuery(int itemId, String userName) {
        query.append("DELETE FROM votes ");
        query.append(" WHERE item_id = ");
        query.append(itemId);
        query.append(" and user_id in (SELECT user_id FROM users WHERE user_name = '");
        query.append(userName);
        query.append("');");
    }


    private int insertData(boolean close)
    {
        int rc = 0;

        try
        {
            stmt = conn.prepareStatement(query.toString());
            stmt.executeUpdate();
            return closeConnection(rc, close);
        }catch (Exception e)
        {
            e.printStackTrace();
            e.getMessage();
            rc = -1;
            return closeConnection(rc, close);
        }
    }

    private int fetchData(String whatToQuery, boolean close)
    {
        int rc;

        try
        {
            stmt = conn.prepareStatement(query.toString());
            ResultSet rset = stmt.executeQuery();

            switch (whatToQuery)
            {
                case "fetchItemsData":
                    rc = fetchItemsData(rset); break;
                case "fetchCheckUserData":
                    rc = fetchUserCheckData(rset); break;
                case "fetchUserData":
                    rc = fetchUserData(rset); break;
                case "fetchAllTagsData":
                    rc = fetchAllTagsData(rset); break;
                case "fetchItemsIds":
                    rc = fetchItemsIds(rset); break;
                case "fetchAvailableItemId":
                    rc = fetchAvailableItemId(rset); break;
                case "fetchUsersComments":
                    rc = fetchUsersComments(rset); break;
                case "fetchUsersVotes":
                    rc = fetchUsersVotes(rset); break;
                case "fetchUsersItemIds":
                    rc = fetchUsersItemIds(rset); break;
                default:
                    rc = 0;//fetchUserData
            }

            return closeConnection(rc, close);
        }catch (Exception e)
        {
            e.printStackTrace();
            e.getMessage();
            rc = -1;
            return closeConnection(rc, close);
        }
    }

    private int closeConnection(int rc, boolean close)
    {
        if (rc == 1 || close)
        {
            try
            {
                conn.close();
            }catch (SQLException e)
            {
                Log.e("ERROR", "The Fetch action did not succeed.\nThe reason is: " +e.getErrorCode()+": "+e.getMessage());
                rc =  -1;
            }
        }
        return rc;
    }

    private int fetchData(String whatToQuery, ArrayList<Item> list,int itemIndex, boolean close)
    {
        int rc = 0;

        try
        {
            stmt = conn.prepareStatement(query.toString());
            ResultSet rset = stmt.executeQuery();

            switch (whatToQuery)
            {
                case "fetchTagsData":
                    rc = fetchTagsData(rset, list, itemIndex); break;
                case "fetchCommentsData":
                    rc = fetchCommentsData(rset, list, itemIndex); break;
                default:
                    rc = 0;
            }
            return closeConnection(rc, close);
        }catch (Exception e)
        {
            e.printStackTrace();
            e.getMessage();
            rc = -1;
            return closeConnection(rc, close);
        }
       /* finally
        {
            if (close || rc == -1)
            {
                try
                {
                    conn.close();
                }catch (SQLException e)
                {
                    Log.e("ERROR", "The Fetch action did not succeed.\nThe reason is: " +e.getErrorCode()+": "+e.getMessage());
                    rc =  -1;
                }
            }

            return rc;
        }*/

    }

    private int fetchUserData(ResultSet rset) throws SQLException
    {
        int themeNum = -1;
        while (rset.next())
        {

            themeNum = rset.getInt("theme");
        }
        return themeNum;
    }

    private int fetchUserCheckData(ResultSet rset) throws SQLException
    {
        int exists = 0;
        while (rset.next())
        {
            MainActivity.setUser(new User(rset.getString("user_name"),rset.getString("email"), rset.getString("type").charAt(0)), 0);
            exists = 1;
        }
        return exists;
    }

    private int fetchAllTagsData(ResultSet rset) throws SQLException
    {
        int rc = -1;

        while (rset.next())
        {
            MainActivity.addTag(rset.getString("tag_name"));
            rc = 0;
        }
        return rc;
    }

    private int fetchTagsData(ResultSet rset, ArrayList<Item> list, int itemIndex) throws SQLException
    {
        int rc = -1, index = 0;

        while (rset.next())
        {
            list.get(itemIndex).getArrayOfTags()[index] = rset.getString("tag_name");
            rc = 0;
            index ++;
        }
        return rc;
    }

    private int fetchCommentsData(ResultSet rset, ArrayList<Item> list, int itemIndex) throws SQLException
    {
        int rc = -1;
        Item item = list.get(itemIndex);
        if (lastTouched == 0)
        {
            item.getComments().clear();
        }
        Item.Comment comment;
        while (rset.next())
        {
            comment = item.new Comment(Util.decodeStringUrl(rset.getString("text")).replaceAll("~", "'"), rset.getInt("stars"), rset.getString("date_added").replaceAll("-", "/"), rset.getString("user_name"));
            comment.setCommentId(rset.getInt("comment_id"));
            comment.setLikes(rset.getInt("likes"));
            comment.setDislikes(rset.getInt("dislikes"));
            item.addComment(comment);
            rc = 0;
        }
        list.set(itemIndex, item);
        return rc;
    }

    private int fetchItemsIds(ResultSet rset) throws SQLException
    {
        int rc = -1;
        while (rset.next())
        {
            itemIds += rset.getInt("item_id");
            if (!rset.isLast())
            {
                itemIds += ", ";
            }

            rc = 0;
        }
        return rc;
    }

    private int fetchUsersComments(ResultSet rset) throws SQLException
    {
        int rc = -1;
        int i = 0;
        while (rset.next())
        {
            MainActivity.getLocalStorage().updateComments(MainActivity.getUser(userIndex), userIndex, rset.getInt("item_id"), rset.getInt("num_of_comments"));
            rc = 0;
            i++;
        }
        return rc;
    }

    private int fetchUsersItemIds(ResultSet rset) throws SQLException
    {
        int rc = -1;
        int i = 0;
        while (rset.next())
        {
            MainActivity.getLocalStorage().updateSecrets(MainActivity.getUser(userIndex), userIndex, rset.getInt("item_id"));
            rc = 0;
            i++;
        }
        return rc;
    }

    private int fetchUsersVotes(ResultSet rset) throws SQLException
    {
        int rc = -1;
        int i = 0;
        while (rset.next())
        {
            MainActivity.getLocalStorage().updateVotes(MainActivity.getUser(userIndex), userIndex, rset.getInt("item_id"), 1);
            rc = 0;
            i++;
        }
        return rc;
    }

    private int fetchAvailableItemId(ResultSet rset) throws SQLException
    {
        int rc = -1;
        while (rset.next())
        {
            rc = rset.getInt("item_id");
        }
        return rc;
    }

    private int fetchItemsData(ResultSet rset) throws SQLException
    {
        String user, text, title = "", date, firstTagName = "", prevTagName = "";
        int itemId, prevItemId = 0, score, numOfComments, arrayInx = 0, rating = 0, numOfVotes;
        int rc = -1, index = 0;
        String[] tagsArray = new String[3];
        Item item = new Item();
        Item.Votes votes = item.new Votes();

        while (rset.next())
        {
            itemId = rset.getInt("item_id");
            user = rset.getString("user_name");
            String temp = rset.getString("text");
            String[] rawText = temp.split("##");

            if (rawText.length == 2)
            {
                title = rawText[0].replaceAll("~","'");
                text = rawText[1].replaceAll("~","'");
            }
            else
            {
                title = "";
                text = rawText[0].replaceAll("~","'");
            }

            date = rset.getString("date");
            score = rset.getInt("score");
            numOfComments = rset.getInt("comments");
            numOfVotes = rset.getInt("votes");
            if (index == 0)
            {
                firstTagName = rset.getString("tag_name");
                rating = 1 + lastTouched;
            }

            if ((itemId != prevItemId && index > 0) )
            {
                item.setVotes(votes);
                tempItemList.add(item);
                rating++;

                votes = item.new Votes();
                arrayInx = 0;
                tagsArray = new String[3];
                tagsArray[arrayInx] = rset.getString("tag_name");
                prevTagName = rset.getString("tag_name");
                firstTagName = rset.getString("tag_name");
                arrayInx++;
                item = new Item(user, text, title, itemId, date, rating, score, numOfComments, numOfVotes, tagsArray);
                getVotes(votes, rset.getString("vote_type"), rset.getString("voter"));
            }
            else
            {
                if (index == 0)
                {
                    prevItemId = itemId;
                }

                if (firstTagName.equals(rset.getString("tag_name")))
                {
                    getVotes(votes, rset.getString("vote_type"), rset.getString("voter"));
                }

                if(!prevTagName.equals(rset.getString("tag_name")))
                {
                    tagsArray[arrayInx] = rset.getString("tag_name");
                    prevTagName = rset.getString("tag_name");
                    arrayInx++;
                }

                item = new Item(user, text, title, prevItemId, date, rating, score, numOfComments, numOfVotes, tagsArray);
            }

            if (rset.isLast())
            {
                item.setVotes(votes);
                tempItemList.add(item);
            }

            prevItemId = itemId;
            index ++;
            rc = 0;
        }
        return rc;
    }

    private void getVotes(Item.Votes votes, String type, String userName) {
        switch (type)
        {
            case "l": votes.getLikes().add(userName); break;
            case "d": votes.getDislikes().add(userName); break;
            case "s": votes.getSad().add(userName); break;
            case "a": votes.getAngry().add(userName); break;
            case "f": votes.getLol().add(userName); break;
        }
    }

    public void insertIntoTagsItemsBunch(){
        //connect();

        query = new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('sex', 1)");
        insertData(false);
        query = new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('school', 1)");
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('teacher', 1)");
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('money', 2)");
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('prostitution', 2)");
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('flight', 3)");// 31 --> flight
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('military', 4)");// 32 --> military
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('sex', 4)");
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('drugs', 5)");
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('weed', 5)");
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('drugs', 6)");
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('death', 7)"); //--> change kill to death
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('animals', 7)"); //--> 33 animals
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('sex', 8)");
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('work-place', 8)");
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('boss', 8)"); //--> 34 boss
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('humor', 9)"); // 35--> humor
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('technology', 10)"); //36 --> technology
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('family', 11)"); //37 -->family
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('guns', 11)");
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('infidelity', 12)");// 38 --> infidelity
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('wisdom', 13)"); // --> 39 wisdom
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('food', 14)");
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('request', 15)"); // 40 --> request
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('concert', 15)");
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('death', 15)");
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('request', 16)");
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('request', 17)");
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('fantasy', 18)");
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('books', 18)");//41 --> books
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('alcohol', 19)"); // 42 --> alcohol
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('request', 19)");
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('request', 20)");
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('sex', 20)");
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('request', 21)");
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('cars', 21)");
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('TV', 22)");
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('request', 22)");
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('request', 23)");
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('relationship', 23)");
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('request', 24)");
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('activity', 24)"); // 43 --> activity
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('gay', 25)");
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('LGBT', 25)");
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('work-place', 26)");
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('pregnancy', 27)");// 44 pregancy
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('food', 28)");
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('sex', 29)");
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('family', 30)");
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('romantics', 30)");
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('sex', 31)");
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('infidelity', 31)");
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('death', 32)");
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('animals', 32)");
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('pregnancy', 33)");
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('infidelity', 34)");
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('lie', 35)"); // 45 --> lie
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('family', 35)");
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('sex', 36)");
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('teacher', 36)");
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('money', 37)");
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('teacher', 38)");
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('sex', 38)");
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('lie', 39)");
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('dream', 40)");// 46 --> dream
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('work-place', 40)");
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('activity', 41)");
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('request', 41)");
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('request', 42)");
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('animals', 42)");
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('pregnancy', 43)");
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('flight', 44)");
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('books', 45)");
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('romantics', 46)");
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('death', 47)");
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('family', 47)");
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('collage', 48)");// 47 --> collage
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('TV', 49)");
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('request', 49)");
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('weed', 50)");
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('drugs', 50)");
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('wisdom', 51)");
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('collage', 52)");
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('collage', 52)");
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('request', 53)");
        insertData(false);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, item_id) VALUES('cars', 54)");
        insertData(false);
    }

    public void insertIntoTagListBunch(){
       /* query = new StringBuffer("delete from tags;");
        insertData(false);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('fantasy'); ");
        insertData(false);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('sex'); ");
        insertData(false);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('gay'); ");
        insertData(false);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('LGBT'); ");
        insertData(false);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('lesbian'); ");
        insertData(false);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('menstruation'); ");
        insertData(false);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('cars'); ");
        insertData(false);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('money'); ");
        insertData(false);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('bike'); ");
        insertData(false);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('sickness'); ");
        insertData(false);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('rock-band'); ");
        insertData(false);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('concert'); ");
        insertData(false);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('prostitution'); ");
        insertData(false);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('transport'); ");
        insertData(false);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('kill'); ");
        insertData(false);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('music'); ");
        insertData(false);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('TV'); ");
        insertData(false);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('work-place'); ");
        insertData(false);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('school'); ");
        insertData(false);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('library'); ");
        insertData(false);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('drugs'); ");
        insertData(false);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('weed'); ");
        insertData(false);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('love'); ");
        insertData(false);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('sports'); ");
        insertData(false);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('football'); ");
        insertData(false);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('romantics'); ");
        insertData(false);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('teacher'); ");
        insertData(false);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('food'); ");
        insertData(false);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('guns'); ");
        insertData(false);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('relationship'); ");
        insertData(false);*/

        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('flight'); ");
        insertData(false);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('military'); ");
        insertData(false);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('animals'); ");
        insertData(false);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('boss'); ");
        insertData(false);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('humor'); ");
        insertData(false);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('technology'); ");
        insertData(false);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('family'); ");
        insertData(false);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('infidelity'); ");
        insertData(false);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('wisdom'); ");
        insertData(false);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('request'); ");
        insertData(false);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('books'); ");
        insertData(false);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('alcohol'); ");
        insertData(false);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('activity'); ");
        insertData(false);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('pregnancy'); ");
        insertData(false);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('lie'); ");
        insertData(false);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('dream'); ");
        insertData(false);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('collage'); ");
        insertData(false);
    }

    public void insertIntoItemBunch(){
        query = new StringBuffer("insert into items (item_id, user_name, text, date, score) values (1, 'Maratik', 'Believe it or not, I don~t care##I fucked my teacher in 7th grade', NOW(), 5);");
        insertData(false);
        query = new StringBuffer("insert into items (item_id, user_name, text, date, score) values (2, 'Maratik', 'Lottery##I won a lottery ticket, and lost all my money on prostitutes', NOW(), 12);");
        insertData(false);
        query = new StringBuffer("insert into items (item_id, user_name, text, date, score) values (3, 'Marat2', 'A dream##trying to get out of this country', NOW(), 10);");
        insertData(false);
        query = new StringBuffer("insert into items (item_id, user_name, text, date, score) values (4, 'Marat2', 'Poor me##I served in the military as male prostitute', NOW(), 8);");
        insertData(false);
        query = new StringBuffer("insert into items (item_id, user_name, text, date, score) values (5, 'Marat2', 'I bought 1 kilogram of weed when I was 12 years old', NOW(), 2);");
        insertData(false);
        query = new StringBuffer("insert into items (item_id, user_name, text, date, score) values (6, 'Marat2', 'DRUGGS!!!##I~m getting stoned every fucking day - Life is great!!!!', NOW(), 9);");
        insertData(false);
        query = new StringBuffer("insert into items (item_id, user_name, text, date, score) values (7, 'Marat2', 'Stupid birds##I have killed a rooster. He fucking made me crazy. Every god damn morning screaming like crazy. Who made this bird?!', NOW(), 7);");
        insertData(false);
        query = new StringBuffer("insert into items (item_id, user_name, text, date, score) values (8, 'Marat2', 'My boss made my life miserable for 5 months, until I force-fucked her after working hours. Now she always smiling. What should I do now?', NOW(), 4);");
        insertData(false);
        query = new StringBuffer("insert into items (item_id, user_name, text, date, score) values (9, 'Maratik', 'Greetings##Greeting humans, I~m an alien from Mars!!!', NOW(), 6);");
        insertData(false);
        query = new StringBuffer("insert into items (item_id, user_name, text, date, score) values (10, 'Maratik', 'I have created a great application, but it is a secret - I don~t know why', NOW(), 11);");
        insertData(false);
        query = new StringBuffer("insert into items (item_id, user_name, text, date, score) values (11, 'Marat2', 'I found a gun while going back home. As it appeared later on my mother-in-law lost something near my house, and every time I see her strolling back and forth. I~m afraid she killed someone with it and forgot to remove her finger prints.', NOW(), 3);");
        insertData(false);
        query = new StringBuffer("insert into items (item_id, user_name, text, date, score) values (12, 'Maratik', 'My wife thinks that I~m he best husband in the world, I~m not revealing who I am, as I~m afraid that other women will sexually harass me and I might cheat', NOW(), 1);");
        insertData(false);
        query = new StringBuffer("insert into items (item_id, user_name, text, date, score) values (13, 'Marat2', 'Smart words##Good things happen to smart people', NOW(), 14);");
        insertData(false);
        query = new StringBuffer("insert into items (item_id, user_name, text, date, score) values (14, 'Maratik', 'Please...##I~m hungry!!! Someone please give food.', NOW(), 13);");
        insertData(false);
        query = new StringBuffer("insert into items (item_id, user_name, text, date, score) values (15, 'Maratik', 'Need a ride##I want to go to the \"Aerosmith\" concert tomorrow in Tel Aviv, someone can geve me a ride from Haifa? Public transportation is on strike, and I have no other way to get there', NOW(), 16);");
        insertData(false);
        query = new StringBuffer("insert into items (item_id, user_name, text, date, score) values (16, 'Marat2', 'Feeling great##Feeling good!!! Who feel the same? please share', NOW(), 15);");
        insertData(false);
        query = new StringBuffer("insert into items (item_id, user_name, text, date, score) values (17, 'Maratik', 'Feeling lonely##I~m feeling lonely. Who else is lonely? let~s talk.', NOW(), 9);");
        insertData(false);
        query = new StringBuffer("insert into items (item_id, user_name, text, date, score) values (18, 'Marat2', 'Just read the best fantasy book ever!!! Called \"The way of kings\", by Brandon Sanderson', NOW(), 8);");
        insertData(false);
        query = new StringBuffer("insert into items (item_id, user_name, text, date, score) values (19, 'Marat2', 'Drink, anybody?##I want to get drunk, but hate drinking alone. I live in Tel Aviv, who wants to join?', NOW(), 10);");
        insertData(false);
        query = new StringBuffer("insert into items (item_id, user_name, text, date, score) values (20, 'Marat2', 'Advertisement##My tongue does not hurt anymore. All you pretty ladies who did not had an orgasm in a while, and want one, please leave your phone number in my facebook page', NOW(), 7); ");
        insertData(false);
        query = new StringBuffer("insert into items (item_id, user_name, text, date, score) values (21, 'Maratik', 'I want to buy a a new car with budget of 30000$. I have a dilemma. Please suggest, what is the best choice for this budget.', NOW(), 11);");
        insertData(false);
//        query = new StringBuffer("insert into items (item_id, user_name, text, date, score) values (21, 'Maratik', 'Looking for recommendations##What is the best Sci-Fi movie you have ever watched?', NOW(), 6);");
//        insertData(false);
        query = new StringBuffer("insert into items (item_id, user_name, text, date, score) values (22, 'Maratik', 'I~m looking for a serious relationship. I work in high-tech industry and will make my chosen lady a queen. Please contact me on facebook or Tweeter', NOW(), 3);");
        insertData(false);
        query = new StringBuffer("insert into items (item_id, user_name, text, date, score) values (23, 'Marat2', 'Looking for new partners for Escape room activity. Please contact me via facebook', NOW(), 12);");
        insertData(false);
        query = new StringBuffer("insert into items (item_id, user_name, text, date, score) values (24, 'Maratik', 'I~m gay, and I~m proud of it!!!', NOW(), 4);");
        insertData(false);
        query = new StringBuffer("insert into items (item_id, user_name, text, date, score) values (25, 'Marat2', 'Starting new job at the White House, Washington DC. I~m so exited!!!', NOW(), 5);");
        insertData(false);
        query = new StringBuffer("insert into items (item_id, user_name, text, date, score) values (26, 'Maratik', 'Hooray!!!!##5 years of treatments. I~m finally pregnant!!! Hooray!!!', NOW(), 2);");
        insertData(false);
        query = new StringBuffer("insert into items (item_id, user_name, text, date, score) values (27, 'Marat2', 'Today I ate the best pizza ever. It~S called Pizza hut, who ate it and loved it as well?', NOW(), 1); ");
        insertData(false);
        query = new StringBuffer("insert into items (item_id, user_name, text, date, score) values (28, 'Maratik', 'Sad childhood##When I was 5 years old, I have seen my father~s friend jerking off while looking at me. I did not realize back then what he was doing, but now I do, but I don~t what to tell by daddy, he still his best friend.', NOW(), 13);");
        insertData(false);
        query = new StringBuffer("insert into items (item_id, user_name, text, date, score) values (29, 'Maratik', 'My first kiss was my brother in law. I think it was a pity kiss, but still it felt great. I was 16 back then.', NOW(), 14);");
        insertData(false);
        query = new StringBuffer("insert into items (item_id, user_name, text, date, score) values (30, 'Marat2', 'Fuck it!##I don~t believe, what happened last night. I accidentally shagged by friend from high school on my wedding day.', NOW(), 15); ");
        insertData(false);
        query = new StringBuffer("insert into items (item_id, user_name, text, date, score) values (31, 'Marat2', 'Oh Shit...##When I was 10 years old, I accidentally killed a cat by pulling to hard on a leash which I put on him', NOW(), 16); ");
        insertData(false);
        query = new StringBuffer("insert into items (item_id, user_name, text, date, score) values (32, 'Marat2', 'I~m going to have a daughter. It~s a secret because I~m very superstitious', NOW(), 17);");
        insertData(false);
        query = new StringBuffer("insert into items (item_id, user_name, text, date, score) values (33, 'Marat2', 'I just cheated on my girlfriend. It~s a weird feeling. as I~m proud and despise myself. The girl is very hot', NOW(), 18);");
        insertData(false);
        query = new StringBuffer("insert into items (item_id, user_name, text, date, score) values (34, 'Marat2', 'When I was 8 years old, I broke my play station and told my parents that it was my older brother who tends to ruin things. They believed me over him, and bought me a new one, a better one. What could I do? I was afraid they won~t by me a new one if they know it was me', NOW(), 19); ");
        insertData(false);
        query = new StringBuffer("insert into items (item_id, user_name, text, date, score) values (35, 'Marat2', 'I~m going to meet my teacher, whom I fucked when I was 15. I hope she still hot as she was 10 years ago. Who know what will happen', NOW(), 20);");
        insertData(false);
        query = new StringBuffer("insert into items (item_id, user_name, text, date, score) values (36, 'Maratik', 'I just won 200000 shekels on a lottery. I~m so happy!', NOW(), 21);");
        insertData(false);
        query = new StringBuffer("insert into items (item_id, user_name, text, date, score) values (37, 'Maratik', 'I just met my teacher, whom I fucked 10 years ago. It~s amazing how people change, or I wasn~t that picky?', NOW(), 22);");
        insertData(false);
        query = new StringBuffer("insert into items (item_id, user_name, text, date, score) values (38, 'Marat2', 'When I was 7, my sister bought me a car. I lost it, but I told her that bullies took it from me, as I did not want her to know that it was my fault', NOW(), 23);");
        insertData(false);
        query = new StringBuffer("insert into items (item_id, user_name, text, date, score) values (39, 'Marat2', 'Another day in the office... Wish I was somewhere far far away ', NOW(), 17);");
        insertData(false);
        query = new StringBuffer("insert into items (item_id, user_name, text, date, score) values (40, 'Maratik', 'Need for 3 people to join our paint ball team for next Sunday. We are located in New York, if you wish to join contact me via facebook', NOW(), 18);");
        insertData(false);
        query = new StringBuffer("insert into items (item_id, user_name, text, date, score) values (41, 'Maratik', 'Looking for a mate for my female Bull Terrier. If you are serious and you male has all necessary credentials, please contact me.', NOW(), 19);");
        insertData(false);
        query = new StringBuffer("insert into items (item_id, user_name, text, date, score) values (42, 'Marat2', 'The day started great, just woke up tp realize that I~ll going to have a son', NOW(), 20);");
        insertData(false);
        query = new StringBuffer("insert into items (item_id, user_name, text, date, score) values (43, 'Maratik', 'Canada, here I come. Just received my PR this morning. I~m so exited!!!', NOW(), 21);");
        insertData(false);
        query = new StringBuffer("insert into items (item_id, user_name, text, date, score) values (44, 'Marat2', 'The new book of Stormlight Archive is coming out tomorrow, I can hardly wait.... So exited', NOW(), 22);");
        insertData(false);
        query = new StringBuffer("insert into items (item_id, user_name, text, date, score) values (45, 'Marat2', 'Just met a girl of my dreams: Tall, blonde and has a great sense of humor. I hope she~ll find me as attractive as I find her. Wish me luck.', NOW(), 23);");
        insertData(false);
        query = new StringBuffer("insert into items (item_id, user_name, text, date, score) values (46, 'Marat2', 'R.I.P by beloved granny. You made my life much better when I was little. Will always love you.', NOW(), 24);");
        insertData(false);
        query = new StringBuffer("insert into items (item_id, user_name, text, date, score) values (47, 'Maratik', 'My son just get into Harvard! I~m so exited for him. We will have a Harvard graduated lawyer in our family. Proud of you, my son.', NOW(), 25);");
        insertData(false);
        query = new StringBuffer("insert into items (item_id, user_name, text, date, score) values (48, 'Maratik', 'Can someone recommend me on a great comedy. All recent movies that I have watched were mediocre at best.', NOW(), 26);");
        insertData(false);
        query = new StringBuffer("insert into items (item_id, user_name, text, date, score) values (49, 'Maratik', 'I~m so happy California legalized weed. Who as happy as I am?', NOW(), 27);");
        insertData(false);
        query = new StringBuffer("insert into items (item_id, user_name, text, date, score) values (50, 'Marat2', 'Donald Trumps is my hero! Not!', NOW(), 28);");
        insertData(false);
        query = new StringBuffer("insert into items (item_id, user_name, text, date, score) values (51, 'Maratik', 'Just discovered my daughter is a lesbian. I hope she will change her mind', NOW(), 29);");
        insertData(false);
        query = new StringBuffer("insert into items (item_id, user_name, text, date, score) values (52, 'Marat2', 'What can I buy to 7 years old nephew, who does not appreciate toys?', NOW(), 30);");
        insertData(false);
        query = new StringBuffer("insert into items (item_id, user_name, text, date, score) values (53, 'Maratik', 'Oh my god. I just witnessed in live a harsh car accident. 3 cars where smashed by a semi-trailer. I hope no one died.', NOW(), 31);");
        insertData(false);
    }

}
