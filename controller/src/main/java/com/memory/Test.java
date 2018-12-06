package com.memory;

import java.lang.reflect.Field;

/**
 * @program parent
 * @Author: cui.Memory
 * @Date: 2018/12/06 21:01
 * @description:
 */
public class Test {
    public static void main(String[] args) {
        Hero hero = new Hero(new Weapon());
        try {
            Field field = hero.getClass().getDeclaredField("weaponMain");
            WeaponHook weaponHook = new WeaponHook();
            weaponHook.setOnUseWeaponAttackListener(damage -> {
                //代码注入
                damage = 100;
                return damage;
            });
            field.setAccessible(true);
            field.set(hero, weaponHook);
        } catch (Exception e) {
            e.printStackTrace();
        }
        hero.attack();
    }
}
class Hero {
    private Weapon weaponMain;
    public Hero(Weapon weaponMain) {
        this.weaponMain = weaponMain;
    }
    public void attack(){
        weaponMain.attack();
    }
}
class Weapon {
    int damage = 10;
    public void attack(){
        System.out.println(String.format("对目标造成 %d 点伤害",damage));
    }
}
class WeaponHook extends Weapon{
    private OnAttackListener onAttackListener;
    @Override
    public void attack(){
        if (onAttackListener != null){
            damage = onAttackListener.onAttackListenerMethod(damage);
        }
        super.attack();
    }
    public void setOnUseWeaponAttackListener(OnAttackListener onAttackListener) {
        this.onAttackListener = onAttackListener;
    }
    public interface OnAttackListener {
        int onAttackListenerMethod(int damage);
    }
}