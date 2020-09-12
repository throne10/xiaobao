package com.xiaobao.good.retrofit.result;

public class VoiceContent {

    /**
     * code : success
     * data : {"analyze":null,"visit_id":32,"voice_comment":null,"voice_content":"现在我开始录音。","voice_file":"32_ef7701c2c4a1473595f55de7011b3817_voiceFile.mp3","voice_id":5}
     * message : null
     */

    private String code;
    private DataBean data;
    private Object message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public static class DataBean {
        /**
         * analyze : null
         * visit_id : 32
         * voice_comment : null
         * voice_content : 现在我开始录音。
         * voice_file : 32_ef7701c2c4a1473595f55de7011b3817_voiceFile.mp3
         * voice_id : 5
         */

        private Object analyze;
        private int visit_id;
        private Object voice_comment;
        private String voice_content;
        private String voice_file;
        private int voice_id;

        public Object getAnalyze() {
            return analyze;
        }

        public void setAnalyze(Object analyze) {
            this.analyze = analyze;
        }

        public int getVisit_id() {
            return visit_id;
        }

        public void setVisit_id(int visit_id) {
            this.visit_id = visit_id;
        }

        public Object getVoice_comment() {
            return voice_comment;
        }

        public void setVoice_comment(Object voice_comment) {
            this.voice_comment = voice_comment;
        }

        public String getVoice_content() {
            return voice_content;
        }

        public void setVoice_content(String voice_content) {
            this.voice_content = voice_content;
        }

        public String getVoice_file() {
            return voice_file;
        }

        public void setVoice_file(String voice_file) {
            this.voice_file = voice_file;
        }

        public int getVoice_id() {
            return voice_id;
        }

        public void setVoice_id(int voice_id) {
            this.voice_id = voice_id;
        }
    }
}
