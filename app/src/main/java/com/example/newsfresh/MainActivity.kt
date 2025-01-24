package com.example.newsfresh

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), NewsItemClicked {
//    private lateinit var recyclerView: RecyclerView
    private lateinit var mAdapter: NewsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        recyclerView=findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager= LinearLayoutManager(this)
        fetchdata()
        mAdapter=NewsListAdapter(this)
        recyclerView.adapter= mAdapter
//        val items = fetchdata()
//        val adapter = NewsListAdapter(items,this)
//        recyclerView.adapter = adapter
    }

    private fun fetchdata(){
//        val list = ArrayList<String>()
//        for (i in 0 until 100){
//            list.add("Item $i")
//        }
//        return list
        val url = "https://newsapi.org/v2/top-headlines?country=us&apiKey=1e80780ba3734b60a168bd9e19d4a1c3"
//        val url = "'https://newsapi.org/v2/everything?' +\n" +
//                "          'q=Apple&' +\n" +
//                "          'from=2021-09-05&' +\n" +
//                "          'sortBy=popularity&' +\n" +
//                "          'apiKey=1e80780ba3734b60a168bd9e19d4a1c3"
//        val url = "https://saurav.tech/NewsAPI/top-headlines/category/health/in.json"
        val jsonObjectRequest = object :JsonObjectRequest(
            Request.Method.GET,
            url,
//            new JSONObject(params),
            null,
            {
                val newsJSONArray = it.getJSONArray("articles")
                val newsArray = ArrayList<News>()
                for (i in 0 until newsJSONArray.length()){
                    val newsJsonObject = newsJSONArray.getJSONObject(i)
                    val news = News(
                        newsJsonObject.getString("title"),
                        newsJsonObject.getString("author"),
                        newsJsonObject.getString("url"),
                        newsJsonObject.getString("urlToImage")
                    )
                    newsArray.add(news)
                }
                mAdapter.updateNews(newsArray)

            },

            {


            })
        {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String>? {
                val headers = HashMap<String, String>()
                headers["User-Agent"] = "Mozilla/5.0"
                return headers
            }
        }


        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)

    }

    override fun onItemClicked(item: News) {
//        Toast.makeText(this,"Clicked item is $item",Toast.LENGTH_LONG).show()

        val builder =  CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(item.url))


    }


}