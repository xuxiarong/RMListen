package dialog

import androidx.fragment.app.FragmentManager
import com.rm.baselisten.dialog.CommonMvFragmentDialog
import com.rm.baselisten.mvvm.BaseViewModel
import com.rm.module_home.R

class MenuDetailDialog(
    commonViewModel: BaseViewModel,
    commonViewModelBrId: Int
) : CommonMvFragmentDialog(commonViewModel, R.layout.home_dialog_menu_detail, commonViewModelBrId) {

    override fun isDialogHasBackground(): Boolean {
        return true
    }
}