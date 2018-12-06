package com.memory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

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
            Weapon weaponHook = new WeaponHook();
            ((WeaponHook) weaponHook).setOnUseWeaponAttackListener(qqq -> {
                //通过后门进行操作，这其实就是我们注入的代码
                qqq = 100;
                return qqq;
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
    private OnUseWeaponAttackListener onUseWeaponAttackListener;

    @Override
    public void attack(){

        if (onUseWeaponAttackListener != null){
            damage = onUseWeaponAttackListener.onUseWeaponAttack(damage);
        }
        super.attack();
    }

    public void setOnUseWeaponAttackListener(OnUseWeaponAttackListener onUseWeaponAttackListener) {
        this.onUseWeaponAttackListener = onUseWeaponAttackListener;
    }

    //这就是我们的后门
    public static interface OnUseWeaponAttackListener {
        int onUseWeaponAttack(int damage);
    }
}