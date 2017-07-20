package com.telyo.finemoodnote.utils;

import com.telyo.finemoodnote.entity.RecyclerPlans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/20.
 */

public class OrderUtils {
    public static List<RecyclerPlans> orderPlansByDate(List<RecyclerPlans> plans,String dateType){
        List<Long> num = new ArrayList<>();
        if (plans != null && plans.size()>1) {
            for (int n = 0;n<plans.size(); n++){
                long dateSize = DateUtil.getLongFromStringDate(plans.get(n).getSet_time(),dateType);
                num.add(dateSize);
                L.e("List<Long> num =" + dateSize);
            }
            if (plans != null && plans.size()>1){
                for (int i = 0; i < plans.size(); i++){
                    for (int j = i+1; j <plans.size();j++){
                        if ((num.get(i) - num.get(j)) < 0){
                            //对时间转化的long对象依照大小排序
                            long tempNumI = num.get(i);
                            long tempNumJ = num.get(j);
                            num.remove(j);
                            num.add(j,tempNumI);
                            num.remove(i);
                            num.add(i,tempNumJ);
                            //根据 i和j 对plans 排序
                            RecyclerPlans tempPlansJ = plans.get(j);
                            RecyclerPlans tempPlansI = plans.get(i);
                            plans.remove(j);
                            plans.add(j,tempPlansI);
                            plans.remove(i);
                            plans.add(i,tempPlansJ);
                            L.d(" 转换了 " + i  + " 轮，第 " + j + "次");
                        }
                    }

                }

            }
            return plans;
        }
        return plans;
    }
}
