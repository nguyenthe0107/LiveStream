package olmo.wellness.android.ui.screen.profile_dashboard.edit_mail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import olmo.wellness.android.R
import olmo.wellness.android.ui.common.components.live_button.PrimaryLiveButton
import olmo.wellness.android.ui.screen.signup_screen.utils.LoaderWithAnimation
import olmo.wellness.android.ui.screen.signup_screen.utils.SpaceCompose
import olmo.wellness.android.ui.theme.*

@Composable
fun CheckMailScreen(
    email: String,
    onSuccess: (Boolean) -> Unit,
    viewModel: CheckMailViewModel = hiltViewModel()){

    viewModel.bindEmail(email)
    val isLoading = viewModel.isLoading.collectAsState()

    val isSuccess = viewModel.isSuccess.collectAsState()
    if(isSuccess.value){
        LaunchedEffect(true){
            onSuccess.invoke(true)
            viewModel.resetState()
        }
    }
    Box(modifier = Modifier
        .background(Color_gray_FF7)
        .fillMaxHeight(0.6f)) {
        Column(
            modifier = Modifier
                .background(Color_gray_FF7).fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween){
            Column(
                modifier = Modifier,
                verticalArrangement = Arrangement.Top){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color_gray_FF7)
                        .padding(marginDouble),
                    horizontalArrangement = Arrangement.Center){
                    Text(
                        text = stringResource(R.string.title_check_mail),
                        style = MaterialTheme.typography.h5,
                        overflow = TextOverflow.Ellipsis,
                        color = Color_Purple_FBC,
                        modifier = Modifier.padding(horizontal = marginDouble),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color_gray_FF7),
                    horizontalArrangement = Arrangement.Center){
                    Text(
                        text = stringResource(R.string.des_check_mail),
                        style = MaterialTheme.typography.subtitle1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color_Purple_FBC,
                        modifier = Modifier.padding(horizontal = marginDouble),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Normal
                    )
                }

                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 58.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    AsyncImage(model = R.drawable.olmo_ic_group_default_place_holder, contentDescription = "", modifier = Modifier.size(151.dp, 150.dp))
                    SpaceCompose(height = 6.dp)
                    Text(text = stringResource(id = R.string.des_img_check_mail), style = MaterialTheme.typography.subtitle2.copy(color = Color.Black))
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Bottom){
                PrimaryLiveButton(modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 55.dp, end = 55.dp, bottom = 20.dp),
                    text = stringResource(R.string.title_button_open_mail),
                    onClickFunc = {
                       viewModel.sendToGetVerifyCode()
                    })
            }
        }
        LoaderWithAnimation(isPlaying = isLoading.value)
    }
}