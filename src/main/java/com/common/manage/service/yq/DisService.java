package com.common.manage.service.yq;


import com.common.manage.dao.yq.AccusationDAO;
import com.common.manage.dao.yq.DiscoveryAuditDAO;
import com.common.manage.dao.yq.DiscoveryDAO;
import com.common.manage.entity.yq.Dis;
import com.common.manage.entity.yq.Discovery;
import com.common.manage.entity.yq.DiscoveryAudit;
import com.xyj.common.tool.BeanInfoUtil;
import com.xyj.common.web.BaseDAO;
import com.xyj.common.web.BaseService;
import com.xyj.common.web.query.QueryDTO;
import com.xyj.common.web.query.SpecificationUtil;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.common.geo.GeoDistance;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.sort.GeoDistanceSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.FacetedPage;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @classDescription:
 * @author:xiayingjie
 * @createTime:15-5-21 17:25
 */
@Service
public class DisService {


    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    /**
     * 搜索接口
     * @param longitude
     * @param latitude
     * @param distance
     * @param type
     * @param category
     * @param queryDTO
     * @return
     */
    public FacetedPage search(double longitude, double latitude, double distance,int type,int reward ,String category,String detail,QueryDTO queryDTO) {

        //按距离排序
        GeoDistanceSortBuilder sort = new GeoDistanceSortBuilder("location");
        sort.unit(DistanceUnit.KILOMETERS);
        sort.order(SortOrder.ASC);
        sort.point(longitude, latitude);
        sort.geoDistance(GeoDistance.ARC);

        //范围过滤
        GeoDistanceFilterBuilder geoFilter = FilterBuilders.geoDistanceFilter("location")
                .point(longitude,latitude)
                .distance(distance, DistanceUnit.KILOMETERS)
                .geoDistance(GeoDistance.ARC);
        AndFilterBuilder andFilterBuilder=FilterBuilders.andFilter(geoFilter);
        //type 1表示寻找，2表示招领
        if(type>0) {
            TermFilterBuilder typeFilter = FilterBuilders.termFilter("type", type);
            andFilterBuilder.add(typeFilter);
        }
        //category 物品类型
        if(!StringUtils.isBlank(category)) {
            TermFilterBuilder categoryFilter = FilterBuilders.termFilter("category", category);
            andFilterBuilder.add(categoryFilter);
        }
        if(reward>0){
            RangeFilterBuilder rewardFilter=FilterBuilders.rangeFilter("reward").gt(0);
            andFilterBuilder.add(rewardFilter);
        }
        QueryBuilder queryBuilder=null;
        if(!StringUtils.isBlank(detail)) {
            queryBuilder=QueryBuilders.matchQuery("detail", detail);

        }
        queryDTO.setPage(queryDTO.getPage() - 1);
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withFilter(andFilterBuilder)
                .withQuery(queryBuilder)
                .withSort(sort)
                .withPageable(new PageRequest(queryDTO.getPage(), queryDTO.getSize()))
                .build();


        FacetedPage fp= elasticsearchTemplate.queryForPage(searchQuery, Dis.class);


        return fp;

    }


}
