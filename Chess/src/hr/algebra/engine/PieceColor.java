/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.engine;

import hr.algebra.engine.model.player.BlackPlayer;
import hr.algebra.engine.model.player.Player;
import hr.algebra.engine.model.player.WhitePlayer;

/**
 *
 * @author fran
 */
public enum PieceColor {
    WHITE {
        @Override
        public boolean isWhite() {
            return true;
        }

        @Override
        public Player choosePlayer(final WhitePlayer whitePlayer, final BlackPlayer blackPlayer) {
            return whitePlayer;
        }
    }, 
    BLACK {
        @Override
        public boolean isWhite() {
            return false;
        }

        @Override
        public Player choosePlayer(final WhitePlayer whitePlayer, final BlackPlayer blackPlayer) {
            return blackPlayer;
        }
    };

    public abstract boolean isWhite();

     public abstract Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer);
}
