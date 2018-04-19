package howto.library.android_livraria_kotlin_graphql

import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.*
import android.widget.BaseAdapter
import android.widget.ListAdapter
import android.widget.ListView
import android.widget.TextView
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

        val livrosLista: MutableList<Book> = mutableListOf()

        client = setupApollo()

        client.query(BooksQuery
                .builder()
                .build())
                .enqueue(object : ApolloCall.Callback<BooksQuery.Data>() {

                    override fun onFailure(e: ApolloException) {
                        Log.info(e.message.toString())
                    }

                    override fun onResponse(response: Response<BooksQuery.Data>) {

                        response.data()?.allBooks()?.forEachIndexed { index, element ->
                            livrosLista.add(Book(response.data()?.allBooks()?.get(index)?.name().toString()
                                    , response.data()?.allBooks()?.get(index)?.description().toString()))
                        }

                    }

                })

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        val lv = findViewById<ListView>(R.id.lvBooks)
        lv.adapter = ListBooksAdapter(livrosLista,this)

    }

    private class ListBooksAdapter(books: List<Book>, context: Context) : BaseAdapter() {

        var sListBooks = books

        private val mInflator: LayoutInflater

        init {
            this.mInflator = LayoutInflater.from(context)
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View? {
            val view: View?
            val vh: ListRowHolder
            if (p1 == null) {
                view = this.mInflator.inflate(R.layout.list_item_book, p2, false)
                vh = ListRowHolder(view)
                view.tag = vh
            } else {
                view = p1
                vh = view.tag as ListRowHolder
            }

            vh.nomeLivro.text = sListBooks[p0].name
            vh.descricaoLivro.text = sListBooks[p0].description
            return view
        }

        override fun getItem(p0: Int): Any {
            return sListBooks[p0]
        }

        override fun getCount(): Int {
            return sListBooks.size
        }

    }

    private class ListRowHolder(row: View?) {
        public val nomeLivro: TextView
        public val descricaoLivro: TextView

        init {
            this.nomeLivro = row?.findViewById<TextView>(R.id.tvNomeLivro) as TextView
            this.descricaoLivro = row?.findViewById<TextView>(R.id.tvDescricaoLivro) as TextView
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
