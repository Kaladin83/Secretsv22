package com.example.maratbe.secretsv2;

/**
 * Created by MARATBE on 12/19/2017.
 */

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

//import com.mysql.jdbc.Connection;


import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;

public class DataBase  {
    public String ip = "sql11.freesqldatabase.com:3306";
    public String classs = "com.mysql.jdbc.Driver";
    public String dbName = "sql11211228";
    public String userName = "sql11211228";
    public String password = "3N8weuq9PZ";
    private  StringBuffer query;
    private PreparedStatement stmt = null;
    private Connection conn = null;

    @SuppressLint("NewApi")

    public DataBase()
    {
        connect();
    }
    public void connect() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String ConnURL = null;
        try {

            Class.forName(classs);
            ConnURL = "jdbc:mysql://" + ip + "/"
                    +dbName+ "?useUnicode=true&characterEncoding=UTF-8" ;
            //ConnURL = "jdbc:mysql://" + ip + "/"+dbName;
            conn = DriverManager.getConnection(ConnURL, userName, password);
        } catch ( SQLException se) {
            Log.e("ERROR", se.getErrorCode()+": "+se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("ERROR", e.getMessage());
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage());
            //SQLException | ClassNotFoundException |
        }
    }
    public int insertUser(String password, int date, char type, String user)
    {
        int returnCode = 0;
        query = new StringBuffer();

        createSelectUserCheckQuery(user);
        if (fetchData("fetchCheckUserData") ==  0)
        {
            query = new StringBuffer();
            createInsertUserQuery(password, date, type, user);
            returnCode = insertData(query);
        }

        return returnCode;
    }

    public int insertTags(String tagName, int tagId, int itemId)
    {
        query = new StringBuffer();

        createInsertTagsQuery(tagName, tagId, itemId);
        return insertData(query);
    }

    public int insertLike(int itemId)
    {
        query = new StringBuffer();

        createInsertLikeStatQuery(itemId);
        return insertData(query);
    }

    public int insertAngry(int itemId)
    {
        query = new StringBuffer();

        createInsertAngryStatQuery(itemId);
        return insertData(query);
    }

    public int insertSad(int itemId)
    {
        query = new StringBuffer();

        createInsertSadStatQuery(itemId);
        return insertData(query);
    }

    public int insertLol(int itemId)
    {
        query = new StringBuffer();

        createInsertLolStatQuery(itemId);
        return insertData(query);
    }


    public int insertComment(int itemId, String text, int parentId)
    {
        int returnCode = 0;
        query = new StringBuffer();
        text = text.replaceAll("'", "\"");

        createInsertCommentQuery(itemId, text, parentId);
        if (insertData(query) == 0)
        {
            query = new StringBuffer();
            createInsertCommentStatQuery(itemId);
            returnCode = insertData(query);
        }

        return returnCode;
    }

    public int updateThemeData(String user, int theme)
    {
        query = new StringBuffer();

        createUpdateThemeQuery(user, theme);

        return insertData(query);
    }

    public int selectTopTenData()
    {
        int returnCode;
        query = new StringBuffer();

        createSelectTopTenQuery();
        returnCode = fetchData("fetchTopTenData");

        return returnCode;
    }

    public int selectTags()
    {
        query = new StringBuffer();

        createSelectAllTagsQuery();
        return fetchData("fetchAllTagsData");
    }


    public int selectTagsData(int itemIndex)
    {
        int returnCode;
        query = new StringBuffer();

        createSelectTagsQuery(itemIndex);
        returnCode = fetchData("fetchTagsData", itemIndex);

        return returnCode;
    }

    public int selectStatisticsData(int itemIndex)
    {
        int returnCode;
        query = new StringBuffer();

        createSelectStatisticsQuery(itemIndex);
        returnCode = fetchData("fetchStatisticsData", itemIndex);

        return returnCode;
    }

    public int selectCommentsData(int itemIndex)
    {
        int returnCode;
        query = new StringBuffer();

        createSelectCommentsQuery(itemIndex);
        returnCode = fetchData("fetchCommentsData", itemIndex);

        return returnCode;
    }

    private void createInsertUserQuery(String password, int date, char type, String user)
    {
        query.append("INSERT INTO users (user_name, password, type, registration_date) VALUES('");
        query.append(user);
        query.append("', '");
        query.append(password);
        query.append("', '");
        query.append(type);
        query.append("', ");
        query.append(date);
        query.append(");");
    }

    private void createInsertTagsQuery(String tagName, int tagId, int itemId)
    {
        query.append("INSERT INTO tags_and_items (tag_name, tag_id, item_id) VALUES(");
        query.append(tagName);
        query.append("', ");
        query.append(tagId);
        query.append(", ");
        query.append(itemId);
        query.append(");");
    }

    private void createInsertCommentQuery(int itemId, String text, int parentId)
    {
        query.append("INSERT INTO comments (text, item_id, parent_id, date_added) VALUES('");
        query.append(text);
        query.append("', ");
        query.append(itemId);
        query.append(", ");
        query.append(parentId);
        query.append(", now()");
        query.append(");");
    }

    private void createInsertAngryStatQuery(int itemId)
    {
        query.append("INSERT INTO statistics (item_id, likes, sad, angry, lol, comments) VALUES(");
        query.append(itemId);
        query.append(", 0, 0, 1, 0, 0) ON DUPLICATE KEY UPDATE ");
        query.append("angry = angry + 1;");
    }

    private void createInsertLolStatQuery(int itemId)
    {
        query.append("INSERT INTO statistics (item_id, likes, sad, angry, lol, comments) VALUES(");
        query.append(itemId);
        query.append(", 0, 0, 0, 1, 0) ON DUPLICATE KEY UPDATE ");
        query.append("lol = lol + 1;");
    }

    private void createInsertSadStatQuery(int itemId)
    {
        query.append("INSERT INTO statistics (item_id, likes, sad, angry, lol, comments) VALUES(");
        query.append(itemId);
        query.append(", 0, 1, 0, 0, 0) ON DUPLICATE KEY UPDATE ");
        query.append("sad = sad + 1;");
    }

    private void createInsertLikeStatQuery(int itemId)
    {
        query.append("INSERT INTO statistics (item_id, likes, sad, angry, lol, comments) VALUES(");
        query.append(itemId);
        query.append(", 1, 0, 0, 0, 0) ON DUPLICATE KEY UPDATE ");
        query.append("likes = likes + 1;");
    }

    private void createInsertCommentStatQuery(int itemId)
    {
        query.append("INSERT INTO statistics (item_id, likes, sad, angry, lol, comments) VALUES(");
        query.append(itemId);
        query.append(", 0, 0, 0, 0, 1) ON DUPLICATE KEY UPDATE ");
        query.append("comments = comments + 1;");
    }

    private void createUpdateStatQuery(String itemId)
    {
        query.append("UPDATE statistics ");
        query.append("SET comments = comments + 1 ");
        query.append("WHERE item_id = '");
        query.append(itemId);
        query.append("';");
    }

    private void createSelectUserCheckQuery(String user)
    {
        query.append("SELECT 1 FROM USERS ");
        query.append("WHERE user_name = '");
        query.append(user);
        query.append("';");
    }

    private void createSelectTopTenQuery()
    {
        query.append("SELECT item.*, IFNULL(tag_name,' ') tag_name, IFNULL(likes,0) likes, IFNULL(comments,0) comments, ");
        query.append("IFNULL(lol,0) lol, IFNULL(angry,0) angry, IFNULL(sad,0) sad ");
        query.append("FROM item left JOIN tags_and_items tags ON ");
        query.append("(item.item_id = tags.item_id) left JOIN ");
        query.append("statistics stat ON ");
        query.append("(item.item_id = stat.item_id) ");
        query.append("WHERE rating <= 10 order by type, rating;");
    }

    /*private void createSelectTopTenQuery()
    {
        query.append("SELECT * FROM item");
        query.append(" WHERE rating <= 10 order by type, rating");
    }*/

    private void createSelectTagsQuery(int itemIndex)
    {
        query.append("SELECT tag_name FROM tags_and_items ");
        query.append("WHERE item_id = ");
        query.append(MainActivity.getItemAt(itemIndex).getItemId());
        query.append(";");
    }

    private void createSelectStatisticsQuery(int itemIndex)
    {
        query.append("SELECT likes, sad, angry, lol, comments FROM statistics ");
        query.append("WHERE item_id = ");
        query.append(MainActivity.getItemAt(itemIndex).getItemId());
        query.append(";");
    }

    private void createSelectCommentsQuery(int itemIndex)
    {
        query.append("SELECT text, date_added FROM comments ");
        query.append("WHERE item_id = ");
        query.append(MainActivity.getItemAt(itemIndex).getItemId());
        query.append(" ORDER BY date_added desc;");
    }


    private void createSelectAllTagsQuery()
    {
        query.append("SELECT tag_id, tag_name FROM tags;");
    }

    private void createUpdateThemeQuery(String userName, int theme)
    {
        query.append("UPDATE USERS ");
        query.append("SET theme = ");
        query.append(theme);
        query.append(" WHERE user_name = '");
        query.append(userName);
        query.append("';");
    }


    private int insertData(StringBuffer whatToInsert)
    {
        int rc = 0;

        try
        {
            stmt = conn.prepareStatement(query.toString());
            stmt.executeUpdate();
        }catch (Exception e)
        {
            e.printStackTrace();
            e.getMessage();
            try
            {
                conn.close();
            }catch (SQLException se)
            {
                Log.e("ERROR", "The Fetch action did not succeed.\nThe reason is: " +se.getErrorCode()+": "+se.getMessage());
            }
            rc = -1;
        }
        return rc;
    }

    private int fetchData(String whatToQuery)
    {
        int rc;

        try
        {
            stmt = conn.prepareStatement(query.toString());
            ResultSet rset = stmt.executeQuery();

            switch (whatToQuery)
            {
                case "fetchTopTenData":
                    rc = fetchTopTenData(rset); break;
                case "fetchCheckUserData":
                    rc = fetchUserCheckData(rset); break;
                case "fetchAllTagsData":
                    rc = fetchAllTagsData(rset); break;
                default:
                    rc = 0;
            }
        }catch (Exception e)
        {
            e.printStackTrace();
            e.getMessage();
            try
            {
                conn.close();
            }catch (SQLException se)
            {
                Log.e("ERROR", "The Fetch action did not succeed.\nThe reason is: " +se.getErrorCode()+": "+se.getMessage());
            }
            rc = -1;
        }
        return rc;
   /*     finally
        {
            try
            {
                 conn.close();
            }catch (SQLException e)
            {
                Log.e("ERROR", "The Fetch action did not succeed.\nThe reason is: " +e.getErrorCode()+": "+e.getMessage());
                rc =  -1;
            }
            return rc;
        }*/

    }

    private int fetchData(String whatToQuery, int itemIndex)
    {
        int rc;

        try
        {
            stmt = conn.prepareStatement(query.toString());
            ResultSet rset = stmt.executeQuery();

            switch (whatToQuery)
            {
                case "fetchTagsData":
                    rc = fetchTagsData(rset, itemIndex); break;
                case "fetchStatisticsData":
                    rc = fetchStatisticsData(rset, itemIndex); break;
                case "fetchCommentsData":
                    rc = fetchCommentsData(rset, itemIndex); break;
                default:
                    rc = 0;
            }
        }catch (Exception e)
        {
            e.printStackTrace();
            e.getMessage();
            try
            {
                conn.close();
            }catch (SQLException se)
            {
                Log.e("ERROR", "The Fetch action did not succeed.\nThe reason is: " +se.getErrorCode()+": "+se.getMessage());
            }
            rc = -1;
        }
        return rc;

    }

    private int fetchUserCheckData(ResultSet rset) throws SQLException
    {
        int exists = 0;
        while (rset.next())
        {
            exists = 1;
        }
        return exists;
    }

    private int fetchAllTagsData(ResultSet rset) throws SQLException
    {
        int rc = -1;

        while (rset.next())
        {
            MainActivity.addTag(new Tag(rset.getInt("tag_id"), rset.getString("tag_name")));
            rc = 0;
        }
        return rc;
    }

    private int fetchTagsData(ResultSet rset, int itemIndex) throws SQLException
    {
        int rc = -1, index = 0;

        while (rset.next())
        {
            MainActivity.getItemAt(itemIndex).getArrayOfTags()[index] = rset.getString("tag_name");
            rc = 0;
            index ++;
        }
        return rc;
    }

    private int fetchStatisticsData(ResultSet rset, int itemIndex) throws SQLException
    {
        int rc = -1;
        int[] emojis = new int[4];

        while (rset.next())
        {
            emojis[0] = rset.getInt("likes");
            emojis[1] = rset.getInt("lol");
            emojis[2] = rset.getInt("sad");
            emojis[3] = rset.getInt("angry");
            MainActivity.getItemAt(itemIndex).setEmojis(emojis);
            MainActivity.getItemAt(itemIndex).setNumOfComments(Integer.parseInt(rset.getString("comments")));
            rc = 0;
        }
        return rc;
    }

    private int fetchCommentsData(ResultSet rset, int itemIndex) throws SQLException
    {
        int rc = -1;
        Item item = new Item();
        Item.Comment comment;
        while (rset.next())
        {
            comment = item.new Comment(rset.getString("text"), rset.getString("date_added"));
            MainActivity.getItemAt(itemIndex).getComments().add(comment);
            rc = 0;
        }
        return rc;
    }


    private int fetchTopTenData(ResultSet rset) throws SQLException
    {
        String user, text;
        char type;
        int itemId, prevItemId = 0, date, rating, comments, arrayInx = 0;
        int rc = -1, index = 0;
        String[] tagsArray = new String[3];
        int[] emojis = new int[4];
        Item item = new Item();

        while (rset.next())
        {
            itemId = Integer.parseInt(rset.getString("item_id"));
            user = rset.getString("user_name");
            text = rset.getString("text");
            type = rset.getString("type").charAt(0);
            date = rset.getInt("date");
            rating = rset.getInt("rating");
            comments = rset.getInt("comments");
            emojis[0] = rset.getInt("likes");
            emojis[1] = rset.getInt("lol");
            emojis[2] = rset.getInt("sad");
            emojis[3] = rset.getInt("angry");
            
            if ((itemId != prevItemId && index > 0) )
            {
                if (index == 18)
                {
                    Log.d("item = 18: ","******** item = 18 ********");
                }
                MainActivity.addItem(item);
                int indexx = (MainActivity.getListOfItemsLength()-1);
                Log.d("item: ","**************************** ["+indexx+"] ****************************" );
                Log.d("item: ","item id = "+item.getItemId());
                Log.d("item: ","Text = "+item.getText());
                Log.d("item: ","comments = "+item.getComments());
                Log.d("item: ","Num of Comments = "+item.getNumOfComments());
                Log.d("item: ","Date = "+item.getDate());
                Log.d("item: ","Rating = "+item.getRating());
                Log.d("item: ","Type = "+item.getType());
                for (int i= 0 ;i < 3;i++)
                {
                    Log.d("item: ","Tag ["+i+"] "+item.getArrayOfTags()[i]);
                }
                for (int i= 0 ;i < 4;i++)
                {
                    Log.d("item: ","Emoji ["+i+"] "+item.getEmojis()[i]);
                }
                arrayInx = 0;
                tagsArray = new String[3];
                tagsArray[arrayInx] = rset.getString("tag_name");

                item = new Item(user, text, type, itemId, date, rating, comments, tagsArray, emojis);

                if (rset.isLast())
                {
                    Log.d("item: ","**************************** ["+indexx+"] ****************************" );
                    Log.d("item: ","item id = "+item.getItemId());
                    Log.d("item: ","Text = "+item.getText());
                    Log.d("item: ","comments = "+item.getComments());
                    Log.d("item: ","Num of Comments = "+item.getNumOfComments());
                    Log.d("item: ","Date = "+item.getDate());
                    Log.d("item: ","Rating = "+item.getRating());
                    Log.d("item: ","Type = "+item.getType());
                    for (int i= 0 ;i < 3;i++)
                    {
                        Log.d("item: ","Tag ["+i+"] "+item.getArrayOfTags()[i]);
                    }
                    for (int i= 0 ;i < 4;i++)
                    {
                        Log.d("item: ","Emoji ["+i+"] "+item.getEmojis()[i]);
                    }
                    MainActivity.addItem(item);
                }
                arrayInx = 0;
            }
            else
            {
                tagsArray[arrayInx] = rset.getString("tag_name");
                item = new Item(user, text, type, prevItemId, date, rating, comments, tagsArray, emojis);
                arrayInx++;
            }

            prevItemId = itemId;
            index ++;
            rc = 0;
        }
        return rc;
    }

    public void insertIntoStatisticsBunch()
    {
        query = new StringBuffer("delete from statistics;");
        insertData(query);
        query = new StringBuffer("INSERT INTO statistics (item_id, likes, comments, sad, angry, lol) VALUES(13, 21, 1, 20, 8, 53)");
        insertData(query);
        query = new StringBuffer("INSERT INTO statistics (item_id, likes, comments, sad, angry, lol) VALUES(1, 50, 2,5,8, 71)");
        insertData(query);
        query = new StringBuffer("INSERT INTO statistics (item_id, likes, comments, sad, angry, lol) VALUES(2, 150, 55, 7, 9, 21)");
        insertData(query);
        query = new StringBuffer("INSERT INTO statistics (item_id, likes, comments, sad, angry, lol) VALUES(3, 73, 7, 3, 0, 4)");
        insertData(query);
        query = new StringBuffer("INSERT INTO statistics (item_id, likes, comments, sad, angry, lol) VALUES(4, 12, 1, 0, 44, 0)");
        insertData(query);
        query = new StringBuffer("INSERT INTO statistics (item_id, likes, comments, sad, angry, lol) VALUES(5, 0, 9, 66, 0, 0)");
        insertData(query);
        query = new StringBuffer("INSERT INTO statistics (item_id, likes, comments, sad, angry, lol) VALUES(6, 130, 66, 67, 35, 81)");
        insertData(query);
        query = new StringBuffer("INSERT INTO statistics (item_id, likes, comments, sad, angry, lol) VALUES(7, 13, 4, 55, 8, 91)");
        insertData(query);
        query = new StringBuffer("INSERT INTO statistics (item_id, likes, comments, sad, angry, lol) VALUES(8, 133, 12, 110, 6, 66)");
        insertData(query);
        query = new StringBuffer("INSERT INTO statistics (item_id, likes, comments, sad, angry, lol) VALUES(9, 0, 8, 66, 77, 0)");
        insertData(query);
        query = new StringBuffer("INSERT INTO statistics (item_id, likes, comments, sad, angry, lol) VALUES(10, 55, 32, 8, 88, 54)");
        insertData(query);
        query = new StringBuffer("INSERT INTO statistics (item_id, likes, comments, sad, angry, lol) VALUES(11, 43, 22, 78, 3, 5)");
        insertData(query);
        query = new StringBuffer("INSERT INTO statistics (item_id, likes, comments, sad, angry, lol) VALUES(12, 89, 0, 0, 0 ,3)");
        insertData(query);
        query = new StringBuffer("INSERT INTO statistics (item_id, likes, comments, sad, angry, lol) VALUES(14, 43, 7, 8, 7, 1)");
        insertData(query);
        query = new StringBuffer("INSERT INTO statistics (item_id, likes, comments, sad, angry, lol) VALUES(15, 250, 25, 0, 5, 44)");
        insertData(query);
        query = new StringBuffer("INSERT INTO statistics (item_id, likes, comments, sad, angry, lol) VALUES(16, 0, 3, 0, 0, 96)");
        insertData(query);
        query = new StringBuffer("INSERT INTO statistics (item_id, likes, comments, sad, angry, lol) VALUES(17, 0, 9, 7, 0, 87)");
        insertData(query);
        query = new StringBuffer("INSERT INTO statistics (item_id, likes, comments, sad, angry, lol) VALUES(18, 81, 30, 99, 7, 6)");
        insertData(query);
        query = new StringBuffer("INSERT INTO statistics (item_id, likes, comments, sad, angry, lol) VALUES(19, 29, 0, 77, 87, 10)");
        insertData(query);
        query = new StringBuffer("INSERT INTO statistics (item_id, likes, comments, sad, angry, lol) VALUES(20, 78, 8, 3, 3, 54)");
        insertData(query);
        query = new StringBuffer("INSERT INTO statistics (item_id, likes, comments, sad, angry, lol) VALUES(21, 37, 13, 77, 6, 81)");
        insertData(query);
        query = new StringBuffer("INSERT INTO statistics (item_id, likes, comments, sad, angry, lol) VALUES(22, 68, 22, 45, 113, 5)");
        insertData(query);
        query = new StringBuffer("INSERT INTO statistics (item_id, likes, comments, sad, angry, lol) VALUES(23, 0, 62, 0, 50, 0)");
        insertData(query);
        query = new StringBuffer("INSERT INTO statistics (item_id, likes, comments, sad, angry, lol) VALUES(24, 101, 20, 32, 22, 41)");
        insertData(query);
    }

    public void insertIntoTagsBunch(){
        query = new StringBuffer("delete from tags_and_items;");
        insertData(query);

        query = new StringBuffer("INSERT INTO tags_and_items (tag_name, tag_id, item_id) VALUES('fantasy', 1, 1)");
        insertData(query);
        query = new StringBuffer("INSERT INTO tags_and_items (tag_name, tag_id, item_id) VALUES('sports', 2, 1)");
        insertData(query);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, tag_id, item_id) VALUES('love', 3, 1)");
        insertData(query);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, tag_id, item_id) VALUES('romantics', 4, 2)");
        insertData(query);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, tag_id, item_id) VALUES('love', 3, 2)");
        insertData(query);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, tag_id, item_id) VALUES('sports', 2, 3)");
        insertData(query);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, tag_id, item_id) VALUES('football', 5, 3)");
        insertData(query);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, tag_id, item_id) VALUES('food', 6, 4)");
        insertData(query);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, tag_id, item_id) VALUES('sex', 7, 5)");
        insertData(query);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, tag_id, item_id) VALUES('sex', 7, 6)");
        insertData(query);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, tag_id, item_id) VALUES('teacher', 8, 6)");
        insertData(query);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, tag_id, item_id) VALUES('work-place', 9, 7)");
        insertData(query);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, tag_id, item_id) VALUES('sex', 7, 7)");
        insertData(query);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, tag_id, item_id) VALUES('dream', 10, 7)");
        insertData(query);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, tag_id, item_id) VALUES('drugs', 11, 8)");
        insertData(query);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, tag_id, item_id) VALUES('weed', 12, 8)");
        insertData(query);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, tag_id, item_id) VALUES('movies', 13, 9)");
        insertData(query);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, tag_id, item_id) VALUES('book', 14, 10)");
        insertData(query);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, tag_id, item_id) VALUES('library', 15, 10)");
        insertData(query);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, tag_id, item_id) VALUES('relationship', 16, 11)");
        insertData(query);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, tag_id, item_id) VALUES('LGBT', 17, 11)");
        insertData(query);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, tag_id, item_id) VALUES('lesbian', 18, 11)");
        insertData(query);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, tag_id, item_id) VALUES('gay', 19, 12)");
        insertData(query);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, tag_id, item_id) VALUES('menstruation', 20, 13)");
        insertData(query);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, tag_id, item_id) VALUES('school', 21, 13)");
        insertData(query);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, tag_id, item_id) VALUES('book', 14, 14)");
        insertData(query);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, tag_id, item_id) VALUES('fantasy', 1, 14)");
        insertData(query);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, tag_id, item_id) VALUES('menstruation', 20, 15)");
        insertData(query);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, tag_id, item_id) VALUES('work-place', 9, 15)");
        insertData(query);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, tag_id, item_id) VALUES('weapons', 22, 16)");
        insertData(query);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, tag_id, item_id) VALUES('kill', 23, 16)");
        insertData(query);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, tag_id, item_id) VALUES('dream', 10, 16)");
        insertData(query);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, tag_id, item_id) VALUES('car', 24, 17)");
        insertData(query);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, tag_id, item_id) VALUES('car', 24, 18)");
        insertData(query);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, tag_id, item_id) VALUES('money', 25, 18)");
        insertData(query);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, tag_id, item_id) VALUES('transport', 26, 19)");
        insertData(query);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, tag_id, item_id) VALUES('concert', 27, 19)");
        insertData(query);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, tag_id, item_id) VALUES('rock-band', 28, 19)");
        insertData(query);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, tag_id, item_id) VALUES('dream', 10, 20)");
        insertData(query);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, tag_id, item_id) VALUES('car', 24, 20)");
        insertData(query);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, tag_id, item_id) VALUES('dream', 10, 21)");
        insertData(query);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, tag_id, item_id) VALUES('bike', 29, 21)");
        insertData(query);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, tag_id, item_id) VALUES('money', 25, 21)");
        insertData(query);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, tag_id, item_id) VALUES('sex', 7, 22)");
        insertData(query);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, tag_id, item_id) VALUES('money', 25, 22)");
        insertData(query);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, tag_id, item_id) VALUES('prostitution', 30, 22)");
        insertData(query);
        query= new StringBuffer("INSERT INTO tags_and_items (tag_name, tag_id, item_id) VALUES('sickness', 31, 22)");
        insertData(query);
    }

    public void insertIntoTagListBunch(){
        query = new StringBuffer("delete from tags;");
        insertData(query);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('fantasy'); ");
        insertData(query);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('sex'); ");
        insertData(query);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('gay'); ");
        insertData(query);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('LGBT'); ");
        insertData(query);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('lesbian'); ");
        insertData(query);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('menstruation'); ");
        insertData(query);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('cars'); ");
        insertData(query);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('money'); ");
        insertData(query);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('bike'); ");
        insertData(query);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('sickness'); ");
        insertData(query);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('rock-band'); ");
        insertData(query);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('concert'); ");
        insertData(query);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('prostitution'); ");
        insertData(query);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('transport'); ");
        insertData(query);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('kill'); ");
        insertData(query);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('music'); ");
        insertData(query);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('TV'); ");
        insertData(query);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('work-place'); ");
        insertData(query);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('school'); ");
        insertData(query);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('library'); ");
        insertData(query);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('drugs'); ");
        insertData(query);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('weed'); ");
        insertData(query);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('love'); ");
        insertData(query);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('sports'); ");
        insertData(query);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('football'); ");
        insertData(query);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('romantics'); ");
        insertData(query);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('teacher'); ");
        insertData(query);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('food'); ");
        insertData(query);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('guns'); ");
        insertData(query);
        query = new StringBuffer("INSERT INTO tags (tag_name) VALUES('relationship'); ");
        insertData(query);
    }
}
