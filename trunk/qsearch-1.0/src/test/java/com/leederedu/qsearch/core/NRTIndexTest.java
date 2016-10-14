package com.leederedu.qsearch.core;


import com.leederedu.qsearch.core.bean.SearchResultBean;
import com.leederedu.qsearch.utils.Constants;
import com.leederedu.qsearch.core.query.PackQuery;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.Query;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by liuwuqiang on 2016/10/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class NRTIndexTest {

    @Autowired
    NRTIndex nrtIndex;

    @Autowired
    NRTSearch nrtSearch;

    @Autowired
    PackQuery packQuery;

    //    @Test
    public void addDocuments() throws Exception {
        nrtIndex.deleteAll();
        nrtIndex.commit();

        long id = 122;
        String title = "测试";
        List<FieldParam> params = new ArrayList<FieldParam>();
        params.add(new FieldParam(FieldName.INTFIELD, Constants.FIELD_NAME.ID, id, Field.Store.YES));
        params.add(new FieldParam(FieldName.STRINGFIELD, Constants.FIELD_NAME.TITLE, title, Field.Store.YES));
        params.add(new FieldParam(FieldName.FLOATFIELD, Constants.FIELD_NAME.BOOST, 1.0f, Field.Store.YES));

        nrtIndex.addDocuments(params);
        nrtIndex.commit();

        String[] fields = new String[]{Constants.FIELD_NAME.TITLE, Constants.FIELD_NAME.TITLE_PINYIN, Constants.FIELD_NAME.TITLE_PINYIN_HEAD};
        Query query = packQuery.getStartQuery(title, fields, BooleanClause.Occur.SHOULD, false);
        SearchResultBean searchResultBean = nrtSearch.search(query, 0, 10);

        assertEquals(1, searchResultBean.getCount());

        Document document = searchResultBean.getDatas().get(0);
        assertEquals(id, Long.parseLong(document.get(Constants.FIELD_NAME.ID)));
        assertEquals(title, document.get(Constants.FIELD_NAME.TITLE));

    }

    @Test
    public void deleteDocument() throws Exception {
        nrtIndex.deleteAll();
        nrtIndex.commit();

        String title = "优能说说233";
        String id = "10002";
        Query query = packQuery.getStringFieldQuery(id, Constants.FIELD_NAME.ID);
        SearchResultBean searchResultBean = nrtSearch.search(query, 0, 10);
        System.out.println(searchResultBean.toString());
        assertEquals(1, searchResultBean.getCount());

        FieldDoc doc = new FieldDoc();
        doc.add(new FieldArg(Constants.FIELD_NAME.ID, id));
        doc.add(new FieldArg(Constants.FIELD_NAME.TITLE, title));
        nrtIndex.addDocument(doc.getDoc());
        nrtIndex.commit();

//        Query query = packQuery.getStringFieldQuery(id, Constants.FIELD_NAME.ID);
         searchResultBean = nrtSearch.search(query, 0, 10);
        System.out.println(searchResultBean.toString());
        assertEquals(1, searchResultBean.getCount());

//        nrtIndex.deleteDocument(id);
//        nrtIndex.commit();
//        SearchResultBean searchResultBean2 = nrtSearch.search(query, 0, 10);
//        assertEquals(0, searchResultBean2.getCount());

//        nrtIndex.deleteAll();
//        nrtIndex.commit();
    }

    @Test
    public void updateDocument() throws Exception {

    }

    //    @Test
    public void search() throws Exception {
        String id = "10002";
        String[] fields = new String[]{Constants.FIELD_NAME.TITLE, Constants.FIELD_NAME.TITLE_PINYIN, Constants.FIELD_NAME.TITLE_PINYIN_HEAD};
        Query query = packQuery.getStringFieldQuery(id, Constants.FIELD_NAME.ID);
        SearchResultBean searchResultBean = nrtSearch.search(query, 0, 10);
        assertEquals(0, searchResultBean.getCount());
    }

}