
package com.announcify.api.text;

import android.content.Context;

public interface FormatEnum {

    public abstract String getExpression();

    public abstract String getSubstitution(Context context, Object object);

}
