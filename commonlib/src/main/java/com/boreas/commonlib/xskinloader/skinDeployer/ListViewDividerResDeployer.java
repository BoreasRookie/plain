package com.boreas.commonlib.xskinloader.skinDeployer;

import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.ListView;

import com.boreas.commonlib.xskinloader.entity.SkinAttr;
import com.boreas.commonlib.xskinloader.entity.SkinConfig;
import com.boreas.commonlib.xskinloader.skinInterface.ISkinResDeployer;
import com.boreas.commonlib.xskinloader.skinInterface.ISkinResourceManager;


/***
 * ListView divider属性的换肤支持（android:divider）
 * Created by Windy on 2018/1/11.
 */

public class ListViewDividerResDeployer implements ISkinResDeployer {

    @Override
    public void deploy(View view, SkinAttr skinAttr, ISkinResourceManager resource) {
        if (!(view instanceof ListView)) {
            return;
        }
        ListView listView = (ListView) view;
        if (SkinConfig.RES_TYPE_NAME_COLOR.equals(skinAttr.attrValueTypeName)) {
            int color = resource.getColor(skinAttr.attrValueRefId);
            listView.setDivider(new ColorDrawable(color));
        } else if (SkinConfig.RES_TYPE_NAME_DRAWABLE.equals(skinAttr.attrValueTypeName)) {
            listView.setDivider(resource.getDrawable(skinAttr.attrValueRefId));
        }
    }
}
