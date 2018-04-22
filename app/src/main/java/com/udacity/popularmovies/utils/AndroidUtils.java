package com.udacity.popularmovies.utils;

import android.text.Layout;
import android.text.TextUtils;
import android.widget.TextView;

/**
 * Created by Mihai on 4/20/2018.
 */

public class AndroidUtils {
    public static boolean isTextViewEllipsized(final TextView textView) {
        boolean result = false;
        if (textView != null) {
            // Check if ellipsizing the text is enabled
            final TextUtils.TruncateAt truncateAt = textView.getEllipsize();
            if (truncateAt != null && !TextUtils.TruncateAt.MARQUEE.equals(truncateAt)) {
                // Retrieve the layout in which the text is rendered
                final Layout layout = textView.getLayout();
                if (layout != null) {
                    // Iterate all lines to search for ellipsized text
                    for (int index = 0; index < layout.getLineCount(); ++index) {
                        // Check if characters have been ellipsized away within this line of text
                        result = layout.getEllipsisCount(index) > 0;
                        // Stop looping if the ellipsis character has been found
                        if (result) {
                            break;
                        }
                    }
                }
            }
        }
        return result;
    }
}
