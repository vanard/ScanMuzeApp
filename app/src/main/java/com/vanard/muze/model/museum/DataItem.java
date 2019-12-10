package com.vanard.muze.model.museum;

import com.google.gson.annotations.SerializedName;

public class DataItem{

	@SerializedName("sumber_dana")
	private String sumberDana;

	@SerializedName("kabupaten_kota")
	private String kabupatenKota;

	@SerializedName("koleksi")
	private String koleksi;

	@SerializedName("desa_kelurahan")
	private String desaKelurahan;

	@SerializedName("lintang")
	private String lintang;

	@SerializedName("status_kepemilikan")
	private String statusKepemilikan;

	@SerializedName("luas_tanah")
	private String luasTanah;

	@SerializedName("alamat_jalan")
	private String alamatJalan;

	@SerializedName("standar")
	private String standar;

	@SerializedName("bujur")
	private String bujur;

	@SerializedName("tahun_berdiri")
	private String tahunBerdiri;

	@SerializedName("kode_pengelolaan")
	private String kodePengelolaan;

	@SerializedName("propinsi")
	private String propinsi;

	@SerializedName("nama")
	private String nama;

	@SerializedName("pengelola")
	private String pengelola;

	@SerializedName("kecamatan")
	private String kecamatan;

	@SerializedName("bangunan")
	private String bangunan;

	@SerializedName("tipe")
	private String tipe;

	@SerializedName("sdm")
	private String sdm;

	@SerializedName("museum_id")
	private String museumId;

	public void setSumberDana(String sumberDana){
		this.sumberDana = sumberDana;
	}

	public String getSumberDana(){
		return sumberDana;
	}

	public void setKabupatenKota(String kabupatenKota){
		this.kabupatenKota = kabupatenKota;
	}

	public String getKabupatenKota(){
		return kabupatenKota;
	}

	public void setKoleksi(String koleksi){
		this.koleksi = koleksi;
	}

	public String getKoleksi(){
		return koleksi;
	}

	public void setDesaKelurahan(String desaKelurahan){
		this.desaKelurahan = desaKelurahan;
	}

	public String getDesaKelurahan(){
		return desaKelurahan;
	}

	public void setLintang(String lintang){
		this.lintang = lintang;
	}

	public String getLintang(){
		return lintang;
	}

	public void setStatusKepemilikan(String statusKepemilikan){
		this.statusKepemilikan = statusKepemilikan;
	}

	public String getStatusKepemilikan(){
		return statusKepemilikan;
	}

	public void setLuasTanah(String luasTanah){
		this.luasTanah = luasTanah;
	}

	public String getLuasTanah(){
		return luasTanah;
	}

	public void setAlamatJalan(String alamatJalan){
		this.alamatJalan = alamatJalan;
	}

	public String getAlamatJalan(){
		return alamatJalan;
	}

	public void setStandar(String standar){
		this.standar = standar;
	}

	public String getStandar(){
		return standar;
	}

	public void setBujur(String bujur){
		this.bujur = bujur;
	}

	public String getBujur(){
		return bujur;
	}

	public void setTahunBerdiri(String tahunBerdiri){
		this.tahunBerdiri = tahunBerdiri;
	}

	public String getTahunBerdiri(){
		return tahunBerdiri;
	}

	public void setKodePengelolaan(String kodePengelolaan){
		this.kodePengelolaan = kodePengelolaan;
	}

	public String getKodePengelolaan(){
		return kodePengelolaan;
	}

	public void setPropinsi(String propinsi){
		this.propinsi = propinsi;
	}

	public String getPropinsi(){
		return propinsi;
	}

	public void setNama(String nama){
		this.nama = nama;
	}

	public String getNama(){
		return nama;
	}

	public void setPengelola(String pengelola){
		this.pengelola = pengelola;
	}

	public String getPengelola(){
		return pengelola;
	}

	public void setKecamatan(String kecamatan){
		this.kecamatan = kecamatan;
	}

	public String getKecamatan(){
		return kecamatan;
	}

	public void setBangunan(String bangunan){
		this.bangunan = bangunan;
	}

	public String getBangunan(){
		return bangunan;
	}

	public void setTipe(String tipe){
		this.tipe = tipe;
	}

	public String getTipe(){
		return tipe;
	}

	public void setSdm(String sdm){
		this.sdm = sdm;
	}

	public String getSdm(){
		return sdm;
	}

	public void setMuseumId(String museumId){
		this.museumId = museumId;
	}

	public String getMuseumId(){
		return museumId;
	}

	@Override
 	public String toString(){
		return 
			"DataItem{" + 
			"sumber_dana = '" + sumberDana + '\'' + 
			",kabupaten_kota = '" + kabupatenKota + '\'' + 
			",koleksi = '" + koleksi + '\'' + 
			",desa_kelurahan = '" + desaKelurahan + '\'' + 
			",lintang = '" + lintang + '\'' + 
			",status_kepemilikan = '" + statusKepemilikan + '\'' + 
			",luas_tanah = '" + luasTanah + '\'' + 
			",alamat_jalan = '" + alamatJalan + '\'' + 
			",standar = '" + standar + '\'' + 
			",bujur = '" + bujur + '\'' + 
			",tahun_berdiri = '" + tahunBerdiri + '\'' + 
			",kode_pengelolaan = '" + kodePengelolaan + '\'' + 
			",propinsi = '" + propinsi + '\'' + 
			",nama = '" + nama + '\'' + 
			",pengelola = '" + pengelola + '\'' + 
			",kecamatan = '" + kecamatan + '\'' + 
			",bangunan = '" + bangunan + '\'' + 
			",tipe = '" + tipe + '\'' + 
			",sdm = '" + sdm + '\'' + 
			",museum_id = '" + museumId + '\'' + 
			"}";
		}
}