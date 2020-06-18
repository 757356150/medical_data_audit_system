/*
 * <summary></summary>
 * <author>He Han</author>
 * <email>hankcs.cn@gmail.com</email>
 * <create-date>2014/12/7 19:02</create-date>
 *
 * <copyright file="DemoSegment.java" company="上海林原信息科技有限公司">
 * Copyright (c) 2003-2014, 上海林原信息科技有限公司. All Right Reserved, http://www.linrunsoft.com/
 * This source is subject to the LinrunSpace License. Please contact 上海林原信息科技有限公司 to get more information.
 * </copyright>
 */
package com.hankcs.demo;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;

import java.util.List;

/**
 * 标准分词
 *
 * @author hankcs
 */
public class DemoSegment
{
    public static void main(String[] args)
    {
        String[] testCase = new String[]{
                "用于血栓栓塞性疾病，如脑血栓形成、脑栓塞、四肢动静脉血栓形成、视网膜静脉栓塞等。",
        };
        for (String sentence : testCase)
        {
            List<Term> termList = HanLP.segment(sentence);
            System.out.println(termList);
        }
    }
}
