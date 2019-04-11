/*
 * Copyleft 2012 Power by colen.
 *
 * Project: app-crm
 * Date: Apr 7, 2012
 */
package com.app.lbs.common.service.pub.impl;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.app.lbs.common.exception.CommonSystemException;
import com.app.lbs.common.service.CommonServiceEx;
import com.app.lbs.common.service.pub.SequenceService;

/**
 * Sequence Service
 *
 * @author colen
 *
 */
public class SequenceServiceImpl extends CommonServiceEx implements SequenceService {

    /** logger */
    private static final Logger LOG = Logger.getLogger(SequenceServiceImpl.class);

    /**
     * Sequence Map
     */
    private Map<String, Sequence> seqMap = null;

    /**
     * initialize
     *
     */
    public void initialize() {

        // query all sequence info
        List<Map<String, Object>> seqlist = getCommonDao().namingQueryForList("sql_common_sequence_queryAll", null);

        seqMap = new HashMap<String, SequenceServiceImpl.Sequence>();
        Sequence seqObj = null;
        for (Map<String, Object> seq: seqlist) {
            seqObj = new Sequence();
            seqObj.seqId = (String) seq.get("seq_id");
            seqObj.min = new BigInteger((String) seq.get("min"));
            seqObj.max = new BigInteger((String) seq.get("max"));
            seqObj.current = new BigInteger((String) seq.get("current"));
            seqObj.next = new BigInteger((String) seq.get("current"));
            seqObj.cycle = "1".equals(seq.get("cycle")) ? true : false;
            seqObj.prefix = seq.get("prefix") == null ? "" : (String) seq.get("prefix");
            seqObj.length = Integer.parseInt(seq.get("length").toString());
            seqObj.padding = "1".equals(seq.get("padding")) ? true : false;
            seqObj.paddingChar = (String) seq.get("padding_char");
            seqObj.bookCnt = new BigInteger(seq.get("subscript_cnt").toString());
            seqObj.step = new BigInteger(seq.get("step").toString());
            // store
            seqMap.put(seqObj.seqId, seqObj);
        }
    }


    @Override
    public String nextSeq(String idKey) {
        Sequence seq = seqMap.get(idKey);
        if (seq == null) {
            LOG.error("can not find Sequence Object by " + idKey);
            throw new CommonSystemException("can not find Sequence Object by " + idKey);
        }

        return seq.next();
    }

    /**
     * Sequence
     *
     * @author colen
     *
     */
    private class Sequence {
        String seqId = null;
        BigInteger min = null;
        BigInteger max = null;
        BigInteger current = null;
        BigInteger next = null;
        BigInteger step = null;
        boolean cycle = false;
        String prefix = null;
        int length = 0;
        boolean padding = true;
        String paddingChar = "0";
        BigInteger bookCnt = null;

        private byte[] lock = new byte[0];
        
        /**
         * next sequence value
         *
         * @return
         */
        public String next() {
            synchronized (lock) {
                if (this.current.compareTo(this.next) < 0) {
                    return this.buildSeq();
                } else {
                    return this.bookAndbuildSeq();
                }
            }
        }

        /**
         * book seq from db and buid it.
         *
         * @return
         */
        private String bookAndbuildSeq() {
            BigInteger pnext = this.current.add(this.bookCnt);
            if (pnext.compareTo(this.max) <= 0) {
                this.next = pnext;
            } else {
                if (this.cycle) {
                    this.current = new BigInteger(this.min.toString());
                    this.next = this.current.add(this.bookCnt);
                } else {
                    LOG.error("Sequence Overflow, idKey=" + this.seqId);
                    throw new CommonSystemException("Sequence Overflow, idKey=" + this.seqId);
                }
            }

            // save to db
            this.bookSeq(this.seqId, this.next.toString());

            // buid
            return this.buildSeq();
        }

        /**
         * book sequence
         *
         * @param key ID Key
         * @param current current value
         */
        private void bookSeq(String key, String current) {
            Map<String, Object> args = new HashMap<String, Object>();
            args.put("id_key", key);
            args.put("current", current);

            // update
            getCommonDao().namingUpdate("sql_common_sequence_update", args);
        }

        /**
         * build sequence
         *
         * @return
         */
        private String buildSeq() {
            // 此处算法可以再优化一下。
            String idx = this.current.toString();
            StringBuilder bud = new StringBuilder(idx);
            if (this.padding) {
                int pdlen = this.length - (this.prefix + idx).length();
                if (pdlen > 0) {
                    while (pdlen-- > 0) {
                        bud.insert(0, this.paddingChar);
                    }
                }
            }
            bud.insert(0, this.prefix);
            this.current = this.current.add(this.step);
            return bud.toString();
        }

    }

}

