import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jp_ais_training.keibo.databinding.FragmentContentsBinding
import com.jp_ais_training.keibo.databinding.FragmentDetailBinding
import com.jp_ais_training.keibo.main.model.Response.ResponseItem

class ContentsFragment() : Fragment() {
    private var targetDate = ""
    private var type = -1 // 0 IncomeFix 1 IncomeFlex 2 ExpenseFix 3 ExpenseFlex
    private var dataArray = ArrayList<ResponseItem>()
    private var parentColorArray = ArrayList<Int>()
    private var colorArray = ArrayList<Int>()

    private var _binding: FragmentContentsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        val bundle = arguments
        if (bundle != null) {
            targetDate = bundle.getString("dateTime").toString()
            type = bundle.getInt("type")
        }
        println("onCreate : $targetDate")

        parentColorArray.add(Color.rgb(255, 204, 204))
        parentColorArray.add(Color.rgb(229, 255, 204))
        parentColorArray.add(Color.rgb(204, 255, 255))
        parentColorArray.add(Color.rgb(229, 204, 255))

        colorArray.add(Color.rgb(255, 225, 225))
        colorArray.add(Color.rgb(250, 255, 225))
        colorArray.add(Color.rgb(225, 255, 255))
        colorArray.add(Color.rgb(250, 225, 255))

        when(type){
            0->{}
            1->{}
            2->{}
            3->{}
        }

        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentContentsBinding.inflate(inflater,container,false)
        binding.test.text=type.toString()+"::"+targetDate.toString()
        return binding.root
    }

    override fun onDestroy() {
        println("onDestroy : $targetDate")
        super.onDestroy()
    }
}