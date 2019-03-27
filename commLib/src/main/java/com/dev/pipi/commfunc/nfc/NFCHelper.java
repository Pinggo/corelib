package com.dev.pipi.commfunc.nfc;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Parcelable;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.dev.pipi.commlib.R;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Locale;

/**
 * <pre>
 *     author : pipi
 *     e-mail : xxx@xx
 *     time   : 2018/09/14
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class NFCHelper {
    private Context context;
    private OnClickListener mClickListener;

    public void setOnClickListener(OnClickListener clickListener) {
        mClickListener = clickListener;
    }

    private NFCHelper(Context context){
        super();
        this.context = context;
    }

    public static NFCHelper newInstance(Context context) {
        return new NFCHelper(context);
    }
    public boolean isSupportOrEnableNfc(){
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(context);
        if (nfcAdapter == null) {
            ToastUtils.showShort("设备不支持NFC！");
            return false;
        }
        if (!nfcAdapter.isEnabled()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(context.getString(R.string.nfc_open))
                    .setMessage(context.getString(R.string.nfc_open_tip))
                    .setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (mClickListener != null) {
                                mClickListener.onCancel();
                            } else {
                                dialog.dismiss();
                            }
                        }
                    })
                    .setPositiveButton(context.getString(R.string.confirm), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_NFC_SETTINGS);
                            context.startActivity(intent);
                        }
                    })
                    .show();
            return false;
        }
        return true;
    }

    /**
     * 读取NFC标签文本数据
     */
    public String readNfcTag(Intent intent) {
        if (!isSupportOrEnableNfc()) return "";
        //1.获取Tag对象
        Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        //2.获取Ndef的实例
        Ndef ndef = Ndef.get(detectedTag);
        Log.i(getClass().getSimpleName(), "type:" + ndef.getType() + ",maxsize:" + ndef.getMaxSize() + " bytes");
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage msgs[] = null;
            int contentSize = 0;
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                    contentSize += msgs[i].toByteArray().length;
                }
            }
            try {
                if (msgs != null) {
                    NdefRecord record = msgs[0].getRecords()[0];
                    String tagText = parseTextRecord(record);
                    Log.i(getClass().getSimpleName(), "tagText:" + tagText + ",contentSize:" + contentSize + " bytes");
                    return tagText;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    /**
     * 解析NDEF文本数据，从第三个字节开始，后面的文本数据
     *
     * @param ndefRecord
     * @return
     */
    private String parseTextRecord(NdefRecord ndefRecord) {
        /**
         * 判断数据是否为NDEF格式
         */
        //判断TNF
        if (ndefRecord.getTnf() != NdefRecord.TNF_WELL_KNOWN) {
            return null;
        }
        //判断可变的长度的类型
        if (!Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
            return null;
        }
        try {
            //获得字节数组，然后进行分析
            byte[] payload = ndefRecord.getPayload();
            //下面开始NDEF文本数据第一个字节，状态字节
            //判断文本是基于UTF-8还是UTF-16的，取第一个字节"位与"上16进制的80，16进制的80也就是最高位是1，
            //其他位都是0，所以进行"位与"运算后就会保留最高位
            String textEncoding = ((payload[0] & 0x80) == 0) ? "UTF-8" : "UTF-16";
            //3f最高两位是0，第六位是1，所以进行"位与"运算后获得第六位
            int languageCodeLength = payload[0] & 0x3f;
            //下面开始NDEF文本数据第二个字节，语言编码
            //获得语言编码
            String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
            //下面开始NDEF文本数据后面的字节，解析出文本
            String textRecord = new String(payload, languageCodeLength + 1,
                    payload.length - languageCodeLength - 1, textEncoding);
            return textRecord;
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * 写数据
     * 描述：TODO
     */
    public void writeNfcTag(Intent intent, String text) {
        if (!isSupportOrEnableNfc()) return;
        if (TextUtils.isEmpty(text)) return;
        //获取Tag对象
        Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        NdefMessage ndefMessage = new NdefMessage(new NdefRecord[]{createTextRecord(text)});
        boolean result = writeTag(ndefMessage, detectedTag);
        Toast.makeText(context, result ? "写入成功" : "写入失败", Toast.LENGTH_SHORT).show();
    }

    /**
     * 创建NDEF文本数据
     *
     * @param text
     * @return
     */
    private NdefRecord createTextRecord(String text) {
        byte[] langBytes = Locale.CHINA.getLanguage().getBytes(Charset.forName("US-ASCII"));
        Charset utfEncoding = Charset.forName("UTF-8");
        //将文本转换为UTF-8格式
        byte[] textBytes = text.getBytes(utfEncoding);
        //设置状态字节编码最高位数为0
        int utfBit = 0;
        //定义状态字节
        char status = (char) (utfBit + langBytes.length);
        byte[] data = new byte[1 + langBytes.length + textBytes.length];
        //设置第一个状态字节，先将状态码转换成字节
        data[0] = (byte) status;
        //设置语言编码，使用数组拷贝方法，从0开始拷贝到data中，拷贝到data的1到langBytes.length的位置
        System.arraycopy(langBytes, 0, data, 1, langBytes.length);
        //设置文本字节，使用数组拷贝方法，从0开始拷贝到data中，拷贝到data的1 + langBytes.length
        //到textBytes.length的位置
        System.arraycopy(textBytes, 0, data, 1 + langBytes.length, textBytes.length);
        //通过字节传入NdefRecord对象
        //NdefRecord.RTD_TEXT：传入类型 读写
        NdefRecord ndefRecord = new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], data);
        return ndefRecord;
    }

    /**
     * 写数据
     *
     * @param ndefMessage 创建好的NDEF文本数据
     * @param tag         标签
     * @return
     */
    private boolean writeTag(NdefMessage ndefMessage, Tag tag) {
        try {
            Ndef ndef = Ndef.get(tag);
            ndef.connect();
            ndef.writeNdefMessage(ndefMessage);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取十六进制，用于标签id
     * 描述：TODO
     * @param intent
     * @return
     */
    public String getRfid(Intent intent) {
        Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        byte[] bytes = detectedTag.getId();
        StringBuilder sb = new StringBuilder();
        for (int i = bytes.length - 1; i >= 0; --i) {
            int b = bytes[i] & 0xff;
            if (b < 0x10)
                sb.append('0');
            sb.append(Integer.toHexString(b));
            if (i > 0)
                sb.append(" ");
        }
        return sb.toString();
    }
}
