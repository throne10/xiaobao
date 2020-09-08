package com.xiaobao.good.retrofit.result;

import java.io.Serializable;
import java.util.List;

public class Clients {
    @Override
    public String toString() {
        return "Clients{" +
                "code='" + code + '\'' +
                ", data=" + data +
                ", message=" + message +
                '}';
    }

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
        @Override
        public String toString() {
            return "DataBean{" +
                    "totalNum=" + totalNum +
                    ", curMonthNum=" + curMonthNum +
                    ", latestRecords=" + latestRecords +
                    ", clients=" + clients +
                    '}';
        }


        private int totalNum;
        private int curMonthNum;
        private LatestRecordsBean latestRecords;
        private List<ClientsBean> clients;

        public int getTotalNum() {
            return totalNum;
        }

        public void setTotalNum(int totalNum) {
            this.totalNum = totalNum;
        }

        public int getCurMonthNum() {
            return curMonthNum;
        }

        public void setCurMonthNum(int curMonthNum) {
            this.curMonthNum = curMonthNum;
        }

        public LatestRecordsBean getLatestRecords() {
            return latestRecords;
        }

        public void setLatestRecords(LatestRecordsBean latestRecords) {
            this.latestRecords = latestRecords;
        }

        public List<ClientsBean> getClients() {
            return clients;
        }

        public void setClients(List<ClientsBean> clients) {
            this.clients = clients;
        }

        public static class LatestRecordsBean {
            @Override
            public String toString() {
                return "LatestRecordsBean{" +
                        "client=" + client +
                        ", client_id=" + client_id +
                        ", employee=" + employee +
                        ", employee_id=" + employee_id +
                        ", img_file=" + img_file +
                        ", purpose='" + purpose + '\'' +
                        ", sign_address='" + sign_address + '\'' +
                        ", visit_id=" + visit_id +
                        ", visit_time='" + visit_time + '\'' +
                        ", wechat_content=" + wechat_content +
                        ", voices=" + voices +
                        '}';
            }


            private ClientBean client;
            private int client_id;
            private EmployeeBean employee;
            private int employee_id;
            private Object img_file;
            private String purpose;
            private String sign_address;
            private int visit_id;
            private String visit_time;
            private Object wechat_content;
            private List<VoicesBean> voices;

            public ClientBean getClient() {
                return client;
            }

            public void setClient(ClientBean client) {
                this.client = client;
            }

            public int getClient_id() {
                return client_id;
            }

            public void setClient_id(int client_id) {
                this.client_id = client_id;
            }

            public EmployeeBean getEmployee() {
                return employee;
            }

            public void setEmployee(EmployeeBean employee) {
                this.employee = employee;
            }

            public int getEmployee_id() {
                return employee_id;
            }

            public void setEmployee_id(int employee_id) {
                this.employee_id = employee_id;
            }

            public Object getImg_file() {
                return img_file;
            }

            public void setImg_file(Object img_file) {
                this.img_file = img_file;
            }

            public String getPurpose() {
                return purpose;
            }

            public void setPurpose(String purpose) {
                this.purpose = purpose;
            }

            public String getSign_address() {
                return sign_address;
            }

            public void setSign_address(String sign_address) {
                this.sign_address = sign_address;
            }

            public int getVisit_id() {
                return visit_id;
            }

            public void setVisit_id(int visit_id) {
                this.visit_id = visit_id;
            }

            public String getVisit_time() {
                return visit_time;
            }

            public void setVisit_time(String visit_time) {
                this.visit_time = visit_time;
            }

            public Object getWechat_content() {
                return wechat_content;
            }

            public void setWechat_content(Object wechat_content) {
                this.wechat_content = wechat_content;
            }

            public List<VoicesBean> getVoices() {
                return voices;
            }

            public void setVoices(List<VoicesBean> voices) {
                this.voices = voices;
            }

            public static class ClientBean implements Serializable {
                @Override
                public String toString() {
                    return "ClientBean{" +
                            "age=" + age +
                            ", client_address='" + client_address + '\'' +
                            ", client_area=" + client_area +
                            ", client_birthday=" + client_birthday +
                            ", client_car='" + client_car + '\'' +
                            ", client_cname=" + client_cname +
                            ", client_email=" + client_email +
                            ", client_house='" + client_house + '\'' +
                            ", client_id=" + client_id +
                            ", client_idcard='" + client_idcard + '\'' +
                            ", client_income=" + client_income +
                            ", client_job='" + client_job + '\'' +
                            ", client_keep=" + client_keep +
                            ", client_label='" + client_label + '\'' +
                            ", client_marriage='" + client_marriage + '\'' +
                            ", client_name='" + client_name + '\'' +
                            ", client_origin=" + client_origin +
                            ", client_phone='" + client_phone + '\'' +
                            ", client_sex='" + client_sex + '\'' +
                            ", client_type='" + client_type + '\'' +
                            ", employee_id=" + employee_id +
                            ", employee_name=" + employee_name +
                            '}';
                }


                private int age;
                private String client_address;
                private Object client_area;
                private long client_birthday;
                private String client_car;
                private Object client_cname;
                private Object client_email;
                private String client_house;
                private int client_id;
                private String client_idcard;
                private int client_income;
                private String client_job;
                private Object client_keep;
                private String client_label;
                private String client_marriage;
                private String client_name;
                private Object client_origin;
                private String client_phone;
                private String client_sex;
                private String client_type;
                private int employee_id;
                private Object employee_name;

                public int getAge() {
                    return age;
                }

                public void setAge(int age) {
                    this.age = age;
                }

                public String getClient_address() {
                    return client_address;
                }

                public void setClient_address(String client_address) {
                    this.client_address = client_address;
                }

                public Object getClient_area() {
                    return client_area;
                }

                public void setClient_area(Object client_area) {
                    this.client_area = client_area;
                }

                public long getClient_birthday() {
                    return client_birthday;
                }

                public void setClient_birthday(long client_birthday) {
                    this.client_birthday = client_birthday;
                }

                public String getClient_car() {
                    return client_car;
                }

                public void setClient_car(String client_car) {
                    this.client_car = client_car;
                }

                public Object getClient_cname() {
                    return client_cname;
                }

                public void setClient_cname(Object client_cname) {
                    this.client_cname = client_cname;
                }

                public Object getClient_email() {
                    return client_email;
                }

                public void setClient_email(Object client_email) {
                    this.client_email = client_email;
                }

                public String getClient_house() {
                    return client_house;
                }

                public void setClient_house(String client_house) {
                    this.client_house = client_house;
                }

                public int getClient_id() {
                    return client_id;
                }

                public void setClient_id(int client_id) {
                    this.client_id = client_id;
                }

                public String getClient_idcard() {
                    return client_idcard;
                }

                public void setClient_idcard(String client_idcard) {
                    this.client_idcard = client_idcard;
                }

                public int getClient_income() {
                    return client_income;
                }

                public void setClient_income(int client_income) {
                    this.client_income = client_income;
                }

                public String getClient_job() {
                    return client_job;
                }

                public void setClient_job(String client_job) {
                    this.client_job = client_job;
                }

                public Object getClient_keep() {
                    return client_keep;
                }

                public void setClient_keep(Object client_keep) {
                    this.client_keep = client_keep;
                }

                public String getClient_label() {
                    return client_label;
                }

                public void setClient_label(String client_label) {
                    this.client_label = client_label;
                }

                public String getClient_marriage() {
                    return client_marriage;
                }

                public void setClient_marriage(String client_marriage) {
                    this.client_marriage = client_marriage;
                }

                public String getClient_name() {
                    return client_name;
                }

                public void setClient_name(String client_name) {
                    this.client_name = client_name;
                }

                public Object getClient_origin() {
                    return client_origin;
                }

                public void setClient_origin(Object client_origin) {
                    this.client_origin = client_origin;
                }

                public String getClient_phone() {
                    return client_phone;
                }

                public void setClient_phone(String client_phone) {
                    this.client_phone = client_phone;
                }

                public String getClient_sex() {
                    return client_sex;
                }

                public void setClient_sex(String client_sex) {
                    this.client_sex = client_sex;
                }

                public String getClient_type() {
                    return client_type;
                }

                public void setClient_type(String client_type) {
                    this.client_type = client_type;
                }

                public int getEmployee_id() {
                    return employee_id;
                }

                public void setEmployee_id(int employee_id) {
                    this.employee_id = employee_id;
                }

                public Object getEmployee_name() {
                    return employee_name;
                }

                public void setEmployee_name(Object employee_name) {
                    this.employee_name = employee_name;
                }
            }

            public static class EmployeeBean {
                /**
                 * employee_id : 4
                 * employee_login : 001
                 * employee_name : 员工1
                 * employee_phone : null
                 * employee_pwd : null
                 * leader_id : 24
                 * role : 1
                 */

                private int employee_id;
                private String employee_login;
                private String employee_name;
                private Object employee_phone;
                private Object employee_pwd;
                private int leader_id;
                private int role;

                public int getEmployee_id() {
                    return employee_id;
                }

                public void setEmployee_id(int employee_id) {
                    this.employee_id = employee_id;
                }

                public String getEmployee_login() {
                    return employee_login;
                }

                public void setEmployee_login(String employee_login) {
                    this.employee_login = employee_login;
                }

                public String getEmployee_name() {
                    return employee_name;
                }

                public void setEmployee_name(String employee_name) {
                    this.employee_name = employee_name;
                }

                public Object getEmployee_phone() {
                    return employee_phone;
                }

                public void setEmployee_phone(Object employee_phone) {
                    this.employee_phone = employee_phone;
                }

                public Object getEmployee_pwd() {
                    return employee_pwd;
                }

                public void setEmployee_pwd(Object employee_pwd) {
                    this.employee_pwd = employee_pwd;
                }

                public int getLeader_id() {
                    return leader_id;
                }

                public void setLeader_id(int leader_id) {
                    this.leader_id = leader_id;
                }

                public int getRole() {
                    return role;
                }

                public void setRole(int role) {
                    this.role = role;
                }
            }

            public static class VoicesBean {
                /**
                 * analyze : true
                 * visit_id : 96
                 * voice_comment : 测试
                 * voice_content : null
                 * voice_file : 96_2f703bfaf7e347068d1808a7c976f6b4_lfasr.wav
                 * voice_id : 50
                 * voice_task : 2f703bfaf7e347068d1808a7c976f6b4
                 * voice_time : 1599021209000
                 */

                private boolean analyze;
                private int visit_id;
                private String voice_comment;
                private Object voice_content;
                private String voice_file;
                private int voice_id;
                private String voice_task;
                private long voice_time;

                public boolean isAnalyze() {
                    return analyze;
                }

                public void setAnalyze(boolean analyze) {
                    this.analyze = analyze;
                }

                public int getVisit_id() {
                    return visit_id;
                }

                public void setVisit_id(int visit_id) {
                    this.visit_id = visit_id;
                }

                public String getVoice_comment() {
                    return voice_comment;
                }

                public void setVoice_comment(String voice_comment) {
                    this.voice_comment = voice_comment;
                }

                public Object getVoice_content() {
                    return voice_content;
                }

                public void setVoice_content(Object voice_content) {
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

                public String getVoice_task() {
                    return voice_task;
                }

                public void setVoice_task(String voice_task) {
                    this.voice_task = voice_task;
                }

                public long getVoice_time() {
                    return voice_time;
                }

                public void setVoice_time(long voice_time) {
                    this.voice_time = voice_time;
                }
            }
        }

        public static class ClientsBean {
            /**
             * age : 23
             * client_address : 宝山
             * client_area : null
             * client_birthday : 871315200000
             * client_car : 是
             * client_cname : null
             * client_email : null
             * client_house : 是
             * client_id : 38
             * client_idcard : 28746637183737
             * client_income : 25
             * client_job : 商人
             * client_keep : null
             * client_label : 旅游
             * client_marriage : 未婚
             * client_name : 张三
             * client_origin : null
             * client_phone : 177373627222
             * client_sex : 男
             * client_type : P
             * employee_id : 4
             * employee_name : 员工1
             */

            private int age;
            private String client_address;
            private Object client_area;
            private long client_birthday;
            private String client_car;
            private Object client_cname;
            private Object client_email;
            private String client_house;
            private int client_id;
            private String client_idcard;
            private int client_income;
            private String client_job;
            private Object client_keep;
            private String client_label;
            private String client_marriage;
            private String client_name;
            private Object client_origin;
            private String client_phone;
            private String client_sex;
            private String client_type;
            private int employee_id;
            private String employee_name;

            public int getAge() {
                return age;
            }

            public void setAge(int age) {
                this.age = age;
            }

            public String getClient_address() {
                return client_address;
            }

            public void setClient_address(String client_address) {
                this.client_address = client_address;
            }

            public Object getClient_area() {
                return client_area;
            }

            public void setClient_area(Object client_area) {
                this.client_area = client_area;
            }

            public long getClient_birthday() {
                return client_birthday;
            }

            public void setClient_birthday(long client_birthday) {
                this.client_birthday = client_birthday;
            }

            public String getClient_car() {
                return client_car;
            }

            public void setClient_car(String client_car) {
                this.client_car = client_car;
            }

            public Object getClient_cname() {
                return client_cname;
            }

            public void setClient_cname(Object client_cname) {
                this.client_cname = client_cname;
            }

            public Object getClient_email() {
                return client_email;
            }

            public void setClient_email(Object client_email) {
                this.client_email = client_email;
            }

            public String getClient_house() {
                return client_house;
            }

            public void setClient_house(String client_house) {
                this.client_house = client_house;
            }

            public int getClient_id() {
                return client_id;
            }

            public void setClient_id(int client_id) {
                this.client_id = client_id;
            }

            public String getClient_idcard() {
                return client_idcard;
            }

            public void setClient_idcard(String client_idcard) {
                this.client_idcard = client_idcard;
            }

            public int getClient_income() {
                return client_income;
            }

            public void setClient_income(int client_income) {
                this.client_income = client_income;
            }

            public String getClient_job() {
                return client_job;
            }

            public void setClient_job(String client_job) {
                this.client_job = client_job;
            }

            public Object getClient_keep() {
                return client_keep;
            }

            public void setClient_keep(Object client_keep) {
                this.client_keep = client_keep;
            }

            public String getClient_label() {
                return client_label;
            }

            public void setClient_label(String client_label) {
                this.client_label = client_label;
            }

            public String getClient_marriage() {
                return client_marriage;
            }

            public void setClient_marriage(String client_marriage) {
                this.client_marriage = client_marriage;
            }

            public String getClient_name() {
                return client_name;
            }

            public void setClient_name(String client_name) {
                this.client_name = client_name;
            }

            public Object getClient_origin() {
                return client_origin;
            }

            public void setClient_origin(Object client_origin) {
                this.client_origin = client_origin;
            }

            public String getClient_phone() {
                return client_phone;
            }

            public void setClient_phone(String client_phone) {
                this.client_phone = client_phone;
            }

            public String getClient_sex() {
                return client_sex;
            }

            public void setClient_sex(String client_sex) {
                this.client_sex = client_sex;
            }

            public String getClient_type() {
                return client_type;
            }

            public void setClient_type(String client_type) {
                this.client_type = client_type;
            }

            public int getEmployee_id() {
                return employee_id;
            }

            public void setEmployee_id(int employee_id) {
                this.employee_id = employee_id;
            }

            public String getEmployee_name() {
                return employee_name;
            }

            public void setEmployee_name(String employee_name) {
                this.employee_name = employee_name;
            }
        }
    }
}
