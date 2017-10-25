package net.iot.helloworld;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class NaverOpenAPIActivity extends AppCompatActivity {
    class Item { //데이터 저장용 클래스
        String title;
        String link;
        String description;
        String bloggername;
        String bloggerlink;
        String postdate;

        Item(String title, String link, String description, String bloggername,
             String bloggerlink, String postdate) {
            this.title = title;
            this.link = link;
            this.description = description;
            this.bloggerlink = bloggerlink;
            this.bloggername = bloggername;
            this.postdate = postdate;
        }
    }

    ArrayList<Item> itemList = new ArrayList<Item>();

    class BlogAdapter extends ArrayAdapter {

        public BlogAdapter(Context context) {
            super(context, R.layout.list_blog_item, itemList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.list_blog_item, null); //XML --> View 객체로 변환
            } else {
                view = convertView; //기존에 변환된 View 객체 재활용
            }

            TextView titleText = (TextView) view.findViewById(R.id.title);
            TextView postdateText = (TextView) view.findViewById(R.id.postdate);
            TextView descriptionText = (TextView) view.findViewById(R.id.description);
            TextView bloggernameText = (TextView) view.findViewById(R.id.bloggername);
            titleText.setText(Html.fromHtml(itemList.get(position).title));
            postdateText.setText(itemList.get(position).postdate);
            descriptionText.setText(Html.fromHtml(itemList.get(position).description));
            bloggernameText.setText(itemList.get(position).bloggername);
            final int pos = position;
            titleText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(itemList.get(pos).link));
                    startActivity(intent);
                }
            });
            bloggernameText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(itemList.get(pos).bloggerlink));
                    startActivity(intent);
                }
            });

            return view;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_naver_open_api);
    }

    public void sendRequest(View view) {
        EditText keywordText = (EditText) findViewById(R.id.keyword);
        new LoadNaverBlogInfo().execute((keywordText.getText().toString()));

    }

    class LoadNaverBlogInfo extends AsyncTask<String, String, String> {
        ProgressDialog dialog = new ProgressDialog(NaverOpenAPIActivity.this);

        @Override
        protected String doInBackground(String... params) {
            String clientId = "CXg62gRwTH2yMDrvGiIm";//애플리케이션 클라이언트 아이디값";
            String clientSecret = "tkCkAyXoH0";//애플리케이션 클라이언트 시크릿값";
            StringBuffer response = new StringBuffer();

            try {
                String text = URLEncoder.encode(params[0], "UTF-8");
                String apiURL = "https://openapi.naver.com/v1/search/blog.json?query=" + text; // json 결과
                URL url = new URL(apiURL);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("X-Naver-Client-Id", clientId);
                con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
                int responseCode = con.getResponseCode();
                BufferedReader br;
                if (responseCode == 200) { // 정상 호출
                    br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                } else {  // 에러 발생
                    br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                }
                String inputLine;
                while ((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                }
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response.toString();
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("네이버 블로그 정보 로딩 중...");
            dialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();
            //Log.i("json",s);

            try {
                //JSON 문자열 -> JSON 객체로 변환
                JSONObject json = new JSONObject(s);
                //JSON 객체에서 items 키값의 배열을 추출 --> 여기에만 해당
                JSONArray items = json.getJSONArray("items");
                itemList.clear(); //동적배열 초기화
                for (int i = 0; i < items.length(); i++) { //tiems 배열 안의 객체 정보 개별 추출
                    JSONObject obj = items.getJSONObject(i);
                    String title = obj.getString("title");
                    String link = obj.getString("link");
                    String bloggerlink = obj.getString("bloggerlink");
                    String description = obj.getString("description");
                    String bloggername = obj.getString("bloggername");
                    String postdate = obj.getString("postdate");
                    itemList.add(new Item(title, link, description, bloggername, bloggerlink, postdate));
                }
                BlogAdapter adapter = new BlogAdapter(NaverOpenAPIActivity.this);
                ListView listView = (ListView)findViewById(R.id.listview);
                listView.setAdapter(adapter);
                /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(itemList.get(position).link));
                        startActivity(intent);
                    }
                });*/
                //Toast.makeText(NaverOpenAPIActivity.this, items.length()+"",Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
