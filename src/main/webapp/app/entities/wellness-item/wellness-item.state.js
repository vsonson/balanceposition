(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('wellness-item', {
            parent: 'entity',
            url: '/wellness-item?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'WellnessItems'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/wellness-item/wellness-items.html',
                    controller: 'WellnessItemController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }]
            }
        })
        .state('wellness-item-detail', {
            parent: 'wellness-item',
            url: '/wellness-item/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'WellnessItem'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/wellness-item/wellness-item-detail.html',
                    controller: 'WellnessItemDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'WellnessItem', function($stateParams, WellnessItem) {
                    return WellnessItem.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'wellness-item',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('wellness-item-detail.edit', {
            parent: 'wellness-item-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wellness-item/wellness-item-dialog.html',
                    controller: 'WellnessItemDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['WellnessItem', function(WellnessItem) {
                            return WellnessItem.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('wellness-item.new', {
            parent: 'wellness-item',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wellness-item/wellness-item-dialog.html',
                    controller: 'WellnessItemDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                desc: null,
                                wellnessvalue: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('wellness-item', null, { reload: 'wellness-item' });
                }, function() {
                    $state.go('wellness-item');
                });
            }]
        })
        .state('wellness-item.edit', {
            parent: 'wellness-item',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wellness-item/wellness-item-dialog.html',
                    controller: 'WellnessItemDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['WellnessItem', function(WellnessItem) {
                            return WellnessItem.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('wellness-item', null, { reload: 'wellness-item' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('wellness-item.delete', {
            parent: 'wellness-item',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wellness-item/wellness-item-delete-dialog.html',
                    controller: 'WellnessItemDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['WellnessItem', function(WellnessItem) {
                            return WellnessItem.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('wellness-item', null, { reload: 'wellness-item' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
