package howto.library.android_livraria_kotlin_graphql

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException

import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import java.util.logging.Logger

class MainActivity : AppCompatActivity() {

    private lateinit var client: ApolloClient

    companion object {
        val Log = Logger.getLogger(MainActivity::class.java.name)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        client = setupApollo()

        client.query(BooksQuery.builder().build())
                .enqueue(object : ApolloCall.Callback<BooksQuery.Data>() {

                    override fun onFailure(e: ApolloException) {
                        Log.info(e.message.toString())
                    }

                    override fun onResponse(response: Response<BooksQuery.Data>) {
                        Log.info(" " + response.data()?.allBooks())
                    }

                })

//        val okHttpClient = OkHttpClient.Builder().build()
//
//        val apolloClient = ApolloClient
//                .builder()
//                .serverUrl("http://10.0.3.2:8080/")
//                .okHttpClient(okHttpClient)
//                .build();
//
//        apolloClient.query(BooksQuery.builder().build()).enqueue(
//                object : ApolloCall.Callback<BooksQuery.Data> () {
//                    override fun onResponse(response: Response<BooksQuery.Data>) {
//                        val books = response.data()?.allBooks
//                        Log.d("sucesso", response.data()?.allBooks().toString())
//                    }
//
//                    override fun onFailure(e: ApolloException) {
//                        Log.d("falha", e.message.toString())
//                    }
//
//                }
//        )

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupApollo(): ApolloClient {
        val okHttp = OkHttpClient
                .Builder()
                .addInterceptor({ chain ->
                    val original = chain.request()
                    val builder = original.newBuilder().method(original.method(),
                            original.body())
                    chain.proceed(builder.build())
                })
                .build()

        return ApolloClient.builder()
                .serverUrl("http://10.0.3.2:8080/graphql")
                .okHttpClient(okHttp)
                .build()
    }
}
