package com.example.mindlog;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.widget.EditText;
import androidx.core.content.ContextCompat;

public class Functions {

    // region fild username
    private static String username = null;
    //endregion

    //region get and set username
    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        Functions.username = username;
    }

    //endregion

    //region navigate to
    public static void navigateTo(Context context, Class<?> targetActivity) {
        Intent intent = new Intent(context, targetActivity);
        context.startActivity(intent);
    }
    //endregion

    //region togglePassoword
    public static void togglePasswordVisibility(EditText passwordEditText, Context context) {
        Drawable rightDrawable = passwordEditText.getCompoundDrawables()[2];
        if (rightDrawable != null) {

            if (passwordEditText.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                passwordEditText.setCompoundDrawablesWithIntrinsicBounds(
                        ContextCompat.getDrawable(context, R.drawable.baseline_password_24),
                        null,
                        ContextCompat.getDrawable(context, R.drawable.baseline_remove_red_eye_24),
                        null
                );
            } else {
                passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                passwordEditText.setCompoundDrawablesWithIntrinsicBounds(
                        ContextCompat.getDrawable(context, R.drawable.baseline_password_24),
                        null,
                        ContextCompat.getDrawable(context, R.drawable.baseline_panorama_fish_eye_24),
                        null
                );
            }
            passwordEditText.setSelection(passwordEditText.getText().length());
        }
    }
    //endregion
}
