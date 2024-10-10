package com.atguigu.tingshu.album.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.tingshu.album.mapper.BaseCategory1Mapper;
import com.atguigu.tingshu.album.mapper.BaseCategory2Mapper;
import com.atguigu.tingshu.album.mapper.BaseCategory3Mapper;
import com.atguigu.tingshu.album.mapper.BaseCategoryViewMapper;
import com.atguigu.tingshu.album.service.BaseCategoryService;
import com.atguigu.tingshu.model.album.BaseCategory1;
import com.atguigu.tingshu.model.album.BaseCategoryView;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class BaseCategoryServiceImpl extends ServiceImpl<BaseCategory1Mapper, BaseCategory1> implements BaseCategoryService {

	List<JSONObject> list = new ArrayList<>();

	@Autowired
	private BaseCategory1Mapper baseCategory1Mapper;

	@Autowired
	private BaseCategory2Mapper baseCategory2Mapper;

	@Autowired
	private BaseCategory3Mapper baseCategory3Mapper;

	@Autowired
	private BaseCategoryViewMapper baseCategoryViewMapper;

	@Override
	public List<JSONObject> getBaseCategoryList() {
		List<BaseCategoryView> baseCategoryViewList = baseCategoryViewMapper.selectList(null);
		//key = category1Id, value = List<BaseCategoryView>
		Map<Long, List<BaseCategoryView>> map = baseCategoryViewList.stream().collect(Collectors.groupingBy(BaseCategoryView::getCategory1Id));
		Iterator<Map.Entry<Long, List<BaseCategoryView>>> iterator = map.entrySet().iterator();
		while(iterator.hasNext()) {
			Map.Entry<Long, List<BaseCategoryView>> entry = iterator.next();
			Long category1Id = entry.getKey();
			List<BaseCategoryView> baseCategoryViewList1 = entry.getValue();
			JSONObject category1 = new JSONObject();
			category1.put("categoryId",category1Id);
			category1.put("categoryName",baseCategoryViewList1.get(0).getCategory1Name());

			Map<Long, List<BaseCategoryView>> map2 = baseCategoryViewList1.stream().collect(Collectors.groupingBy(BaseCategoryView::getCategory2Id));
			List<JSONObject> category2List = new ArrayList<>();
			Iterator<Map.Entry<Long, List<BaseCategoryView>>> iterator1 = map2.entrySet().iterator();
			while(iterator1.hasNext()) {
				Map.Entry<Long, List<BaseCategoryView>> entry1 = iterator1.next();
				Long category2Id = entry1.getKey();
				List<BaseCategoryView> baseCategoryViewList2 = entry1.getValue();
				JSONObject category2 = new JSONObject();
				category2.put("categoryId",category2Id);
				category2.put("categoryName",baseCategoryViewList2.get(0).getCategory2Name());

				List<JSONObject> baseCategoryViewList3 = baseCategoryViewList2.stream().map(baseCategoryView -> {
					JSONObject category3 = new JSONObject();
					category3.put("categoryId",baseCategoryView.getCategory3Id());
					category3.put("categoryName",baseCategoryView.getCategory3Name());
					return category3;
				}).collect(Collectors.toList());

				category2.put("categoryChild",baseCategoryViewList3);

				category2List.add(category2);
			}
			category1.put("categoryChild",category2List);
			list.add(category1);
		}
		return null;
	}
}
